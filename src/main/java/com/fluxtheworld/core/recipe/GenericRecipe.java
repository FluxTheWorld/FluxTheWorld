package com.fluxtheworld.core.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public interface GenericRecipe extends Recipe<RecipeInput> {

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
   * @deprecated Replaced by GenericRecipe implementation method to support any inputs and outputs.
   */
  @Override
  @Deprecated
  default ItemStack assemble(RecipeInput input, HolderLookup.Provider lookupProvider) {
    return ItemStack.EMPTY;
  }

  /**
   * @deprecated Replaced by GenericRecipe implementation method to support any inputs and outputs.
   */
  @Override
  @Deprecated
  default ItemStack getResultItem(HolderLookup.Provider lookupProvider) {
    return ItemStack.EMPTY;
  }

  /**
   * @deprecated Replaced by GenericRecipe implementation method to support any inputs and outputs.
   */
  @Override
  @Deprecated
  default boolean matches(RecipeInput input, Level level) {
    return false;
  }

  /**
   * @deprecated Vanilla method we do not use it.
   */
  @Override
  @Deprecated
  default NonNullList<ItemStack> getRemainingItems(RecipeInput input) {
    return NonNullList.of(ItemStack.EMPTY);
  }

  /**
   * @deprecated Vanilla method we do not use it.
   */
  @Override
  @Deprecated
  default boolean isIncomplete() {
    return false;
  }

}
