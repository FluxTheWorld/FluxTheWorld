package com.fluxtheworld.core.common.block;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.commons.lang3.NotImplementedException;

import com.fluxtheworld.core.common.block_entity.FTWBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FTWEntityBlock<BE extends FTWBlockEntity> extends BaseEntityBlock {

  private final Supplier<BlockEntityType<? extends BE>> typeSupplier;

  protected FTWEntityBlock(Supplier<BlockEntityType<? extends BE>> typeSupplier, Properties properties) {
    super(properties);
    this.typeSupplier = typeSupplier;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return typeSupplier.get().create(blockPos, blockState);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> blockEntityType) {
    return createTickerHelper(blockEntityType, typeSupplier.get(), FTWBlockEntity::tick);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    throw new NotImplementedException("Block codecs are a later problem...");
  }
}
