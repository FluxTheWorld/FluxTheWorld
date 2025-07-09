package com.fluxtheworld.core.common.registry;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityTypeHolder<BE extends BlockEntity> {

  private final String name;
  private final Supplier<BlockEntityType<BE>> factory;

  public BlockEntityTypeHolder(String name, Supplier<BlockEntityType<BE>> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  public Supplier<BlockEntityType<BE>> factory() {
    return this.factory;
  }

  public static class Builder<BE extends BlockEntity> {

    private @Nullable String name;
    private @Nullable BlockEntityType.BlockEntitySupplier<BE> factory;
    private @Nullable Set<Supplier<? extends Block>> blocks;

    public Builder() {
    }

    public Builder<BE> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<BE> factory(BlockEntityType.BlockEntitySupplier<BE> factory) {
      this.factory = factory;
      return this;
    }

    @SafeVarargs
    public final Builder<BE> blocks(Supplier<? extends Block>... blocks) {
      this.blocks = Set.of(blocks);
      return this;
    }

    public BlockEntityTypeHolder<BE> build() {
      Objects.requireNonNull(this.name, "name is null");
      Objects.requireNonNull(this.factory, "factory is null");
      Objects.requireNonNull(this.blocks, "blocksSupplier is null");
      return new BlockEntityTypeHolder<BE>(this.name, () -> new BlockEntityType<BE>(this.factory,
          this.blocks.stream().map((it) -> it.get()).collect(Collectors.toSet()), null));
    }

  }
}
