package com.fluxtheworld.core.storage.stack_adapter;

import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStackAdapter implements StackAdapter<FluidStack> {

  public static final FluidStackAdapter INSTANCE = new FluidStackAdapter();

  private FluidStackAdapter() {
  }

  @Override
  public FluidStack getEmpty() {
    return FluidStack.EMPTY;
  }

  @Override
  public boolean isEmpty(FluidStack stack) {
    return stack.isEmpty();
  }

  @Override
  public int getAmount(FluidStack stack) {
    return stack.getAmount();
  }

  @Override
  public FluidStack copy(FluidStack stack) {
    return stack.copy();
  }

  @Override
  public FluidStack copyWithAmount(FluidStack stack, int amount) {
    return stack.copyWithAmount(amount);
  }

  @Override
  public void grow(FluidStack stack, int amount) {
    stack.grow(amount);
  }

  @Override
  public void shrink(FluidStack stack, int amount) {
    stack.shrink(amount);
  }

  @Override
  public boolean isSameContent(FluidStack stack1, FluidStack stack2) {
    return FluidStack.isSameFluidSameComponents(stack1, stack2);
  }
}