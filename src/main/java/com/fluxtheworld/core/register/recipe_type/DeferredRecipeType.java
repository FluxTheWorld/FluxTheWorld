package com.fluxtheworld.core.register.recipe_type;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredRecipeType<T extends RecipeType<?>> extends DeferredHolder<RecipeType<?>, T> {

  protected DeferredRecipeType(ResourceKey<RecipeType<?>> key) {
    super(key);
  }

  // region Utils

  // endregion
}