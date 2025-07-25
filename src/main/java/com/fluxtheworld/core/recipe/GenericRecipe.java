package com.fluxtheworld.core.recipe;

import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface GenericRecipe<T extends RecipeInput> extends Recipe<T> {

  List<OutputStack> craft(T input, RegistryAccess registryAccess);

  List<OutputStack> getResultStacks(RegistryAccess registryAccess);

  default float experienceReward() {
    return 0;
  }

  @Override
  default boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }

  @Override
  default boolean isSpecial() {
    return true;
  }

  /**
   * @deprecated Replaced by {@link #craft(T, RegistryAccess)} to support multiple outputs.
   *             Vanilla api does not support multiple outputs
   */
  @Override
  @Deprecated
  default ItemStack assemble(T input, HolderLookup.Provider lookupProvider) {
    return ItemStack.EMPTY;
  }

  /**
   * @deprecated Replaced by {@link #getResultStacks(RegistryAccess)} to support multiple outputs.
   *             Vanilla api does not support multiple outputs
   */
  @Deprecated
  @Override
  default ItemStack getResultItem(HolderLookup.Provider lookupProvider) {
    return ItemStack.EMPTY;
  }

}
