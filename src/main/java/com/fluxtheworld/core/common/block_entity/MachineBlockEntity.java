package com.fluxtheworld.core.common.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends GenericBlockEntity implements MenuProvider {

  public MachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
  }

  // region MenuProvider

  @Override
  public Component getDisplayName() {
    return getBlockState().getBlock().getName();
  }

  // endregion
}
