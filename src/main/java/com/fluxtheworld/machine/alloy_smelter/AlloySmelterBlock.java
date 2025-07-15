package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.common.block.MachineBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class AlloySmelterBlock extends MachineBlock<AlloySmelterBlockEntity> {

  public AlloySmelterBlock(BlockBehaviour.Properties properties) {
    super(AlloySmelterRegistry.BLOCK_ENTITY_TYPE, properties);
  }

}
