package com.fluxtheworld.core.storage;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * Adapter interface for abstracting stack operations across different stack types.
 * This allows StackStorage to work with different stack implementations (ItemStack, FluidStack, etc.)
 * without requiring them to implement a common interface.
 * 
 * @param <T> The type of stack this adapter handles
 */
public interface StackAdapter<T> {

  /**
   * Creates an empty instance of the stack type.
   * 
   * @return An empty stack instance
   */
  T getEmpty();

  /**
   * Checks if the given stack is empty.
   * 
   * @param stack The stack to check
   * @return True if the stack is empty
   */
  boolean isEmpty(T stack);

  /**
   * Gets the amount/count of the given stack.
   * 
   * @param stack The stack to measure
   * @return The amount of content in the stack
   */
  int getCount(T stack);

  /**
   * Creates a copy of the given stack.
   * 
   * @param stack The stack to copy
   * @return A copy of the stack
   */
  T copy(T stack);

  /**
   * Creates a copy of the given stack with a specific amount.
   * 
   * @param stack The stack to copy
   * @param amount The amount for the new stack
   * @return A copy of the stack with the specified amount
   */
  T copyWithCount(T stack, int amount);

  /**
   * Increases the amount of the given stack by the specified amount.
   * This method modifies the stack in place.
   * 
   * @param stack The stack to modify
   * @param amount The amount to add
   */
  void grow(T stack, int amount);

  /**
   * Decreases the amount of the given stack by the specified amount.
   * This method modifies the stack in place.
   * 
   * @param stack The stack to modify
   * @param amount The amount to remove
   */
  void shrink(T stack, int amount);

  /**
   * Checks if two stacks contain the same content (ignoring amount).
   * For items, this typically checks if they are the same item type.
   * For fluids, this checks if they are the same fluid type.
   * 
   * @param stack1 The first stack
   * @param stack2 The second stack
   * @return True if the stacks contain the same content type
   */
  boolean isSameContent(T stack1, T stack2);

  int getMaxStackSize(T stack);

  CompoundTag save(Provider provider, T stack);

  T parse(Provider provider, CompoundTag tag);
}