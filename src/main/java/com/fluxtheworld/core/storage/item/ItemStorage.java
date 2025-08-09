package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.ItemStackAdapter;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ItemStorage extends StackStorage<ItemStack> implements INBTSerializable<CompoundTag> {

  private final NonNullList<ItemStack> stacks;
  private final @Nullable ChangeListener changeListener;

  public ItemStorage(ItemSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable ChangeListener changeListener) {
    super(ItemStackAdapter.INSTANCE, slotAccess);
    this.changeListener = changeListener;
    this.stacks = NonNullList.withSize(slotAccess.getSlotCount(), ItemStack.EMPTY);
  }

  @Override
  protected void onContentsChanged(int slot) {
    if (changeListener != null) {
      changeListener.onItemStorageChanged(slot);
    }
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    this.validateSlotIndex(slot);
    return this.stacks.get(slot);
  }

  @Override
  public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (!this.isValid(slot, stack) || !this.canInsert(slot)) {
      return stack;
    }

    this.validateSlotIndex(slot);

    ItemStack existing = this.stacks.get(slot);
    int limit = this.getStackLimit(slot, stack);

    if (!existing.isEmpty()) {
      if (!ItemStack.isSameItemSameComponents(stack, existing)) {
        return stack;
      }
      limit -= existing.getCount();
    }

    if (limit <= 0) {
      return stack;
    }

    boolean reachedLimit = stack.getCount() > limit;

    if (!simulate) {
      if (existing.isEmpty()) {
        this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack.copy());
      }
      else {
        existing.grow(reachedLimit ? limit : stack.getCount());
      }
      this.onContentsChanged(slot);
    }

    return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
  }

  @Override
  public ItemStack extract(int slot, int amount, boolean simulate) {
    if (amount == 0 || !this.canExtract(slot)) {
      return ItemStack.EMPTY;
    }

    this.validateSlotIndex(slot);

    ItemStack existing = this.stacks.get(slot);

    if (existing.isEmpty()) {
      return ItemStack.EMPTY;
    }

    int toExtract = Math.min(amount, existing.getMaxStackSize());

    if (existing.getCount() <= toExtract) {
      if (!simulate) {
        this.stacks.set(slot, ItemStack.EMPTY);
        this.onContentsChanged(slot);
        return existing;
      }
      else {
        return existing.copy();
      }
    }
    else {
      if (!simulate) {
        this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
        this.onContentsChanged(slot);
      }

      return existing.copyWithCount(toExtract);
    }
  }

  @Override
  protected void setStackInSlot(int slot, ItemStack stack) {
    this.validateSlotIndex(slot);
    this.stacks.set(slot, stack);
    this.onContentsChanged(slot);
  }

  private int getStackLimit(int slot, ItemStack stack) {
    return Math.min(this.getSlotCapacity(slot), stack.getMaxStackSize());
  }

  public StackStorage<ItemStack> getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
    return new PipeItemStorage(this, slotAccess, sideAccess, side);
  }

  // region Serialization

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    ListTag items = new ListTag();
    for (int i = 0; i < this.stacks.size(); i++) {
      ItemStack stack = this.stacks.get(i);
      if (!stack.isEmpty()) {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putInt("Slot", i);
        items.add(stack.save(provider, itemTag));
      }
    }

    CompoundTag tag = new CompoundTag();
    tag.put("Items", items);
    return tag;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag tag) {
    ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
    for (int i = 0; i < items.size(); i++) {
      CompoundTag item = items.getCompound(i);

      int slot = item.getInt("Slot");
      this.validateSlotIndex(slot);

      ItemStack.parse(provider, item).ifPresent(stack -> stacks.set(slot, stack));
    }
  }

  // endregion

  public interface ChangeListener {
    void onItemStorageChanged(int slot);
  }

}