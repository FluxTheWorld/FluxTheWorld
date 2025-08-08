package com.fluxtheworld.core.storage.slot_access;

import net.neoforged.neoforge.fluids.FluidStack;

public class FluidSlotAccessConfig extends SlotAccessConfig<FluidStack> {

  private FluidSlotAccessConfig(Builder builder) {
    super(builder.rules, builder.namedSlots, builder.taggedSlots);
  }

  @Override
  public boolean isValid(int slot, FluidStack stack) {
    if (slot < 0 || slot >= this.getSlotCount()) {
      return false;
    }

    return this.rules.get(slot).isValid(stack);
  }

  /**
   * @deprecated Use {@link #isValid(int, FluidStack)} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isFluidValid(int slot, FluidStack stack) {
    return isValid(slot, stack);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends SlotAccessConfig.Builder<FluidStack, Builder, FluidSlotAccessConfig> {

    @Override
    public FluidSlotAccessConfig build() {
      return new FluidSlotAccessConfig(this);
    }
  }
}