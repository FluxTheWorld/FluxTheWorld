package com.fluxtheworld.core.common.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class EnergyMachineBlockEntity extends MachineBlockEntity {

  public EnergyMachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
  }

}
