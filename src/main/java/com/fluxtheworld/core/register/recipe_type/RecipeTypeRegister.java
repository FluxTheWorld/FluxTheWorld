package com.fluxtheworld.core.register.recipe_type;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
public class RecipeTypeRegister extends DeferredRegister<RecipeType<?>> {

  public RecipeTypeRegister(String namespace) {
    super(Registries.RECIPE_TYPE, namespace);
  }

  @Override
  public <T extends RecipeType<?>> DeferredRecipeType<T> register(String name, Function<ResourceLocation, ? extends T> func) {
    return (DeferredRecipeType<T>) super.register(name, func);
  }

  @Override
  public <T extends RecipeType<?>> DeferredRecipeType<T> register(String name, Supplier<? extends T> sup) {
    return this.register(name, key -> sup.get());
  }

  @Override
  protected <T extends RecipeType<?>> DeferredRecipeType<T> createHolder(ResourceKey<? extends Registry<RecipeType<?>>> registryKey, ResourceLocation key) {
    return new DeferredRecipeType<>(ResourceKey.create(registryKey, key));
  }

  // region Utils

  // endregion

}