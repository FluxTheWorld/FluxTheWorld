package com.fluxtheworld.core.storage;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public abstract class StackStorage<T> extends AbstractStackStorage<T> implements INBTSerializable<CompoundTag> {

  private final NonNullList<T> slots;

  protected StackStorage(StackAdapter<T> adapter, int slotCount) {
    super(adapter);
    this.slots = NonNullList.withSize(slotCount, adapter.getEmpty());
  }

  @Override
  public int getSlotCount() {
    return this.slots.size();
  }

  public T getStackInSlot(int slot) {
    validateSlotIndex(slot);
    return this.slots.get(slot);
  }

  public T insert(int slot, T stack, boolean simulate) {
    this.validateSlotIndex(slot);

    if (adapter.isEmpty(stack)) {
      return adapter.getEmpty();
    }

    if (!this.isValid(slot, stack) || !this.canInsert(slot)) {
      return stack;
    }

    T existing = this.slots.get(slot);
    int limit = this.getStackLimit(slot, stack);

    if (!adapter.isEmpty(existing)) {
      if (!adapter.isSameContent(stack, existing)) {
        return stack;
      }
      limit -= adapter.getCount(existing);
    }

    if (limit <= 0) {
      return stack;
    }

    boolean reachedLimit = adapter.getCount(stack) > limit;

    if (!simulate) {
      if (adapter.isEmpty(existing)) {
        this.slots.set(slot, reachedLimit ? adapter.copyWithCount(stack, limit) : adapter.copy(stack));
      }
      else {
        adapter.grow(existing, reachedLimit ? limit : adapter.getCount(stack));
      }
      this.onContentsChanged(slot);
    }

    return reachedLimit ? adapter.copyWithCount(stack, adapter.getCount(stack) - limit) : adapter.getEmpty();
  }

  public T extract(int slot, int amount, boolean simulate) {
    this.validateSlotIndex(slot);

    if (amount == 0 || !this.canExtract(slot)) {
      return adapter.getEmpty();
    }

    T existing = this.slots.get(slot);

    if (adapter.isEmpty(existing)) {
      return adapter.getEmpty();
    }

    int toExtract = Math.min(amount, adapter.getMaxStackSize(existing));

    if (adapter.getCount(existing) <= toExtract) {
      if (!simulate) {
        this.slots.set(slot, adapter.getEmpty());
        this.onContentsChanged(slot);
        return existing;
      }
      else {
        return adapter.copy(existing);
      }
    }
    else {
      if (!simulate) {
        this.slots.set(slot, adapter.copyWithCount(existing, adapter.getCount(existing) - toExtract));
        this.onContentsChanged(slot);
      }

      return adapter.copyWithCount(existing, toExtract);
    }
  }

  public void setStackInSlot(int slot, T stack) {
    this.validateSlotIndex(slot);
    this.slots.set(slot, stack);
    this.onContentsChanged(slot);
  }

  protected void onContentsChanged(int slot) {
  }

  // region Serialization

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    ListTag slots = new ListTag();
    for (int i = 0; i < this.slots.size(); i++) {
      T stack = this.slots.get(i);
      if (!adapter.isEmpty(stack)) {
        CompoundTag slot = new CompoundTag();
        slot.putInt("I", i);
        slot.put("S", adapter.save(provider, stack));

        slots.add(slot);
      }
    }

    CompoundTag tag = new CompoundTag();
    tag.put("Slots", slots);
    return tag;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag tag) {
    @SuppressWarnings("rawtypes")
    CollectionTag<CompoundTag> slots = (CollectionTag) tag.getList("Slots", Tag.TAG_COMPOUND);

    for (CompoundTag slot : slots) {
      int i = slot.getInt("I");
      this.validateSlotIndex(i);

      @Nullable
      T stack = adapter.parse(provider, slot.getCompound("S"));
      if (stack != null) {
        this.slots.set(i, stack);
      }
    }
  }

  // endregion
}
