package com.fluxtheworld.common.block.entity;

import com.fluxtheworld.common.registry.FTWBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends BlockEntity {

  public AlloySmelterBlockEntity(BlockPos pos, BlockState state) {
    super(FTWBlockEntities.MY_BLOCK_ENTITY.get(), pos, state);
  }
}