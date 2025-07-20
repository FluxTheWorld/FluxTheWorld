package com.fluxtheworld.core.register.block_entity_type;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityTypeHolder<B extends BlockEntity> {

  private final String name;
  private final Supplier<BlockEntityType<B>> factory;

  public BlockEntityTypeHolder(String name, Supplier<BlockEntityType<B>> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  public Supplier<BlockEntityType<B>> factory() {
    return this.factory;
  }

  public static class Builder<B extends BlockEntity> {

    private @Nullable String name;
    private @Nullable BlockEntityType.BlockEntitySupplier<B> factory;
    private @Nullable Set<Supplier<? extends Block>> blocks;

    public Builder<B> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<B> factory(BlockEntityType.BlockEntitySupplier<B> factory) {
      this.factory = factory;
      return this;
    }

    @SafeVarargs
    public final Builder<B> blocks(Supplier<? extends Block>... blocks) {
      this.blocks = Set.of(blocks);
      return this;
    }

    @SuppressWarnings("null")
    public BlockEntityTypeHolder<B> build() {
      Objects.requireNonNull(this.name, "name is null");
      Objects.requireNonNull(this.factory, "factory is null");
      Objects.requireNonNull(this.blocks, "blocksSupplier is null");
      return new BlockEntityTypeHolder<>(this.name, () -> new BlockEntityType<B>(this.factory,
          this.blocks.stream().map(Supplier::get).collect(Collectors.toSet()), null));
    }

  }
}
