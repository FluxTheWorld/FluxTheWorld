package com.fluxtheworld.core.storage.slot_access;

import net.minecraft.world.item.ItemStack;

public class ItemSlotAccessConfig extends SlotAccessConfig<ItemStack> {

  private ItemSlotAccessConfig(Builder builder) {
    super(builder.rules, builder.namedSlots, builder.taggedSlots);
  }

  @Override
  public boolean isValid(int slot, ItemStack stack) {
    if (slot < 0 || slot >= this.getSlotCount()) {
      return false;
    }

    return this.rules.get(slot).isValid(stack);
  }

  /**
   * @deprecated Use {@link #isValid(int, ItemStack)} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isItemValid(int slot, ItemStack stack) {
    return isValid(slot, stack);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends SlotAccessConfig.Builder<ItemStack, Builder, ItemSlotAccessConfig> {

    @Override
    public ItemSlotAccessConfig build() {
      return new ItemSlotAccessConfig(this);
    }
  }
}
