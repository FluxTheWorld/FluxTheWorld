package com.fluxtheworld.registry.core;

import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item;

public class ItemHolder<I extends Item> {

  private final String name;
  private final Supplier<I> factory;

  public ItemHolder(String name, Supplier<I> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  public Supplier<I> factory() {
    return this.factory;
  }

  public static class Builder<I extends Item> {

    private @Nullable String name;
    private @Nullable Supplier<I> factory;

    public Builder() {
    }

    public Builder<I> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<I> factory(Supplier<I> factory) {
      this.factory = factory;
      return this;
    }

    @SuppressWarnings("null")
    public ItemHolder<I> build() {
      Objects.requireNonNull(this.name, "name is null");
      Objects.requireNonNull(this.factory, "factory is null");
      return new ItemHolder<>(this.name, this.factory);
    }

  }
}
