package com.fluxtheworld.core.register.recipe_serializer;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
public class RecipeSerializerRegister extends DeferredRegister<RecipeSerializer<?>> {

  public RecipeSerializerRegister(String namespace) {
    super(Registries.RECIPE_SERIALIZER, namespace);
  }

  @Override
  public <T extends RecipeSerializer<?>> DeferredRecipeSerializer<T> register(String name, Function<ResourceLocation, ? extends T> func) {
    return (DeferredRecipeSerializer<T>) super.register(name, func);
  }

  @Override
  public <T extends RecipeSerializer<?>> DeferredRecipeSerializer<T> register(String name, Supplier<? extends T> sup) {
    return this.register(name, key -> sup.get());
  }

  @Override
  protected <T extends RecipeSerializer<?>> DeferredRecipeSerializer<T> createHolder(ResourceKey<? extends Registry<RecipeSerializer<?>>> registryKey, ResourceLocation key) {
    return new DeferredRecipeSerializer<>(ResourceKey.create(registryKey, key));
  }

  // region Utils

  // endregion

}