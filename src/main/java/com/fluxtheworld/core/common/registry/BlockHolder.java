package com.fluxtheworld.core.common.registry;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.world.level.block.Block;

public class BlockHolder<B extends Block> implements Supplier<B> {

  private final String name;
  private final Supplier<B> factory;

  public BlockHolder(String name, Supplier<B> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  @Override
  public B get() {
    return this.factory.get();
  }

  public static class Builder<B extends Block> {

    private @Nullable String name;
    private @Nullable Supplier<B> factory;

    public Builder() {
    }

    public Builder<B> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<B> factory(Supplier<B> factory) {
      this.factory = factory;
      return this;
    }

    public BlockHolder<B> build() {
      Preconditions.checkNotNull(this.name, "name is null");
      Preconditions.checkNotNull(this.factory, "factory is null");
      return new BlockHolder<>(name, factory);
    }

  }
}
