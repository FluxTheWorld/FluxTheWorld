package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.core.common.block.MachineBlock;
import com.fluxtheworld.registry.BlockEntityTypeRegistry;
import com.fluxtheworld.registry.core.BlockHolder;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class AlloySmelterBlock extends MachineBlock<AlloySmelterBlockEntity> {

  public AlloySmelterBlock(BlockBehaviour.Properties properties) {
    super(BlockEntityTypeRegistry.ALLOY_SMELTER, properties);
  }

}
