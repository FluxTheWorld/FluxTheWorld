package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.FTWRegistry;
import com.fluxtheworld.core.common.block.MachineBlock;
import com.fluxtheworld.core.common.registry.BlockHolder;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class AlloySmelterBlock extends MachineBlock<AlloySmelterBlockEntity> {

  public AlloySmelterBlock(BlockBehaviour.Properties properties) {
    super(FTWRegistry.ALLOY_SMELTER_BLOCK_ENTITY, properties);
  }

}
