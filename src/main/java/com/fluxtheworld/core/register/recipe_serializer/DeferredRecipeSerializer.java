package com.fluxtheworld.core.register.recipe_serializer;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredRecipeSerializer<T extends RecipeSerializer<?>> extends DeferredHolder<RecipeSerializer<?>, T> {

  protected DeferredRecipeSerializer(ResourceKey<RecipeSerializer<?>> key) {
    super(key);
  }

  // region Utils

  // endregion
}