package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.common.block_entity.MachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends MachineBlockEntity {

  public AlloySmelterBlockEntity(BlockPos worldPosition, BlockState blockState) {
    super(AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get(), worldPosition, blockState);
  }

  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new AlloySmelterMenu(containerId, playerInventory, this);
  }

}