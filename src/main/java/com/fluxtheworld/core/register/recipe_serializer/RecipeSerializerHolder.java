package com.fluxtheworld.core.register.recipe_serializer;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeSerializerHolder<M extends RecipeSerializer<?>> {

  private final String name;
  private final Function<ResourceLocation, M> factory;

  public RecipeSerializerHolder(String name, Function<ResourceLocation, M> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  public Function<ResourceLocation, M> factory() {
    return this.factory;
  }

  public static class Builder<M extends RecipeSerializer<?>> {

    private @Nullable String name;
    private @Nullable Function<ResourceLocation, M> factory;

    public Builder<M> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<M> factory(Function<ResourceLocation, M> factory) {
      this.factory = factory;
      return this;
    }

    @SuppressWarnings("null")
    public RecipeSerializerHolder<M> build() {
      Preconditions.checkNotNull(this.name, "name is null");
      Preconditions.checkNotNull(this.factory, "factory is null");
      return new RecipeSerializerHolder<>(this.name, this.factory);
    }

  }
}