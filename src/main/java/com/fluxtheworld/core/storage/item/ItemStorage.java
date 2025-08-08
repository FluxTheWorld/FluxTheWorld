package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.ItemStackAdapter;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ItemStorage extends StackStorage<ItemStack> {

  private final NonNullList<ItemStack> stacks;
  private final @Nullable ChangeListener changeListener;

  public ItemStorage(ItemSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable ChangeListener changeListener) {
    super(slotAccess, ItemStackAdapter.INSTANCE);
    this.changeListener = changeListener;
    this.stacks = NonNullList.withSize(slotAccess.getSlotCount(), ItemStack.EMPTY);
  }

  @Override
  protected void onChanged(int slot) {
    if (changeListener != null) {
      changeListener.onItemStorageChanged(slot);
    }
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    validateSlotIndex(slot);
    return this.stacks.get(slot);
  }

  @Override
  public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (!isValid(slot, stack)) {
      return stack;
    }

    validateSlotIndex(slot);

    ItemStack existing = this.stacks.get(slot);
    int limit = getStackLimit(slot, stack);

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
      onChanged(slot);
    }

    return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
  }

  @Override
  public ItemStack extract(int slot, int amount, boolean simulate) {
    if (amount == 0) {
      return ItemStack.EMPTY;
    }

    validateSlotIndex(slot);

    ItemStack existing = this.stacks.get(slot);

    if (existing.isEmpty()) {
      return ItemStack.EMPTY;
    }

    int toExtract = Math.min(amount, existing.getMaxStackSize());

    if (existing.getCount() <= toExtract) {
      if (!simulate) {
        this.stacks.set(slot, ItemStack.EMPTY);
        onChanged(slot);
        return existing;
      }
      else {
        return existing.copy();
      }
    }
    else {
      if (!simulate) {
        this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
        onChanged(slot);
      }

      return existing.copyWithCount(toExtract);
    }
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    ListTag nbtTagList = new ListTag();
    for (int i = 0; i < stacks.size(); i++) {
      if (!stacks.get(i).isEmpty()) {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putInt("Slot", i);
        nbtTagList.add(stacks.get(i).save(provider, itemTag));
      }
    }
    CompoundTag nbt = new CompoundTag();
    nbt.put("Items", nbtTagList);
    return nbt;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
    for (int i = 0; i < tagList.size(); i++) {
      CompoundTag itemTags = tagList.getCompound(i);
      int slot = itemTags.getInt("Slot");

      if (slot >= 0 && slot < stacks.size()) {
        ItemStack.parse(provider, itemTags).ifPresent(stack -> stacks.set(slot, stack));
      }
    }
  }

  protected int getStackLimit(int slot, ItemStack stack) {
    return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
  }

  protected void validateSlotIndex(int slot) {
    if (slot < 0 || slot >= stacks.size()) {
      throw new IllegalArgumentException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }
  }

  public interface ChangeListener {
    void onItemStorageChanged(int slot);
  }

}