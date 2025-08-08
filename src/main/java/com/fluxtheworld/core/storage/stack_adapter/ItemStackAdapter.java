package com.fluxtheworld.core.storage.stack_adapter;

import net.minecraft.world.item.ItemStack;

public class ItemStackAdapter implements StackAdapter<ItemStack> {

  public static final ItemStackAdapter INSTANCE = new ItemStackAdapter();

  private ItemStackAdapter() {
  }

  @Override
  public ItemStack getEmpty() {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isEmpty(ItemStack stack) {
    return stack.isEmpty();
  }

  @Override
  public int getAmount(ItemStack stack) {
    return stack.getCount();
  }

  @Override
  public ItemStack copy(ItemStack stack) {
    return stack.copy();
  }

  @Override
  public ItemStack copyWithAmount(ItemStack stack, int amount) {
    return stack.copyWithCount(amount);
  }

  @Override
  public void grow(ItemStack stack, int amount) {
    stack.grow(amount);
  }

  @Override
  public void shrink(ItemStack stack, int amount) {
    stack.shrink(amount);
  }

  @Override
  public boolean isSameContent(ItemStack stack1, ItemStack stack2) {
    return ItemStack.isSameItemSameComponents(stack1, stack2);
  }
}