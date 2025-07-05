package com.fluxtheworld.common.block;

import com.fluxtheworld.common.block.entity.AlloySmelterBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlock extends BaseEntityBlock {
  public static final MapCodec<AlloySmelterBlock> CODEC = BlockBehaviour.simpleCodec(AlloySmelterBlock::new);

  public AlloySmelterBlock(BlockBehaviour.Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AlloySmelterBlockEntity(pos, state);
  }
}
