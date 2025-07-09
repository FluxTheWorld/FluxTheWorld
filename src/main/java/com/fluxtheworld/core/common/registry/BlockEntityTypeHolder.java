package com.fluxtheworld.core.common.registry;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityTypeHolder<BE extends BlockEntity> implements Supplier<BlockEntityType<BE>> {

  private final String name;
  private final Supplier<BlockEntityType<BE>> factory;

  public BlockEntityTypeHolder(String name, Supplier<BlockEntityType<BE>> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  @Override
  public BlockEntityType<BE> get() {
    return this.factory.get();
  }

  public static class Builder<BE extends BlockEntity> {

    private @Nullable String name;
    private @Nullable Supplier<BlockEntityType<BE>> factory;

    public Builder() {
    }

    public Builder<BE> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<BE> factory(Supplier<BlockEntityType<BE>> factory) {
      this.factory = factory;
      return this;
    }

    public BlockEntityTypeHolder<BE> build() {
      Preconditions.checkNotNull(this.name, "name is null");
      Preconditions.checkNotNull(this.factory, "factory is null");
      return new BlockEntityTypeHolder<>(name, factory);
    }

  }
}
