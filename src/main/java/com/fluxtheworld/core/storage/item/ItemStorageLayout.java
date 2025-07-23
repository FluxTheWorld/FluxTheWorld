package com.fluxtheworld.core.storage.item;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

// TODO: Rename to SlotAccessConfig
//       Also create SlotAccessMode or SlotAccessRule
public class ItemStorageLayout {
  private final int slotCount;
  private final Predicate<Integer> canInsert;
  private final Predicate<Integer> canExtract;
  private final SlotRule[] slotRules;

  private ItemStorageLayout(int slotCount, Predicate<Integer> canInsert, Predicate<Integer> canExtract, SlotRule[] slotRules) {
    this.slotCount = slotCount;
    this.canInsert = canInsert;
    this.canExtract = canExtract;
    this.slotRules = slotRules;
  }

  public int getSlotCount() {
    return slotCount;
  }

  public boolean isItemValid(int slot, ItemStack stack) {
    if (slot < 0 || slot >= slotCount) {
      return false;
    }
    return slotRules[slot].itemFilter.test(stack);
  }

  public int getStackLimit(int slot) {
    if (slot < 0 || slot >= slotCount) {
      return 0;
    }
    return slotRules[slot].stackLimit;
  }

  public boolean canInsert(int slot) {
    return canInsert.test(slot);
  }

  public boolean canExtract(int slot) {
    return canExtract.test(slot);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private int slotCount = 0;
    private Predicate<Integer> canInsert = i -> true;
    private Predicate<Integer> canExtract = i -> true;
    private SlotRule[] slotRules = new SlotRule[0];

    public Builder slotCount(int slotCount) {
      this.slotCount = slotCount;
      this.slotRules = new SlotRule[slotCount];
      for (int i = 0; i < slotCount; i++) {
        this.slotRules[i] = new SlotRule();
      }
      return this;
    }

    public Builder setCanInsert(Predicate<Integer> canInsert) {
      this.canInsert = canInsert;
      return this;
    }

    public Builder setCanExtract(Predicate<Integer> canExtract) {
      this.canExtract = canExtract;
      return this;
    }

    public Builder addSlotRule(int slot, Predicate<ItemStack> itemFilter, int stackLimit) {
      if (slot < 0 || slot >= slotCount) {
        throw new IndexOutOfBoundsException("Slot " + slot + " is out of bounds for " + slotCount + " slots.");
      }
      this.slotRules[slot] = new SlotRule(itemFilter, stackLimit);
      return this;
    }

    public ItemStorageLayout build() {
      return new ItemStorageLayout(slotCount, canInsert, canExtract, slotRules);
    }
  }

  private static class SlotRule {
    private final Predicate<ItemStack> itemFilter;
    private final int stackLimit;

    public SlotRule() {
      this(stack -> true, 64);
    }

    public SlotRule(Predicate<ItemStack> itemFilter, int stackLimit) {
      this.itemFilter = itemFilter;
      this.stackLimit = stackLimit;
    }
  }
}
