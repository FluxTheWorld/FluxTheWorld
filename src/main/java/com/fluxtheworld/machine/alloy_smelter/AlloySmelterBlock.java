package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.common.block.MachineBlock;
import com.fluxtheworld.core.common.register.BlockEntityTypeRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class AlloySmelterBlock extends MachineBlock<AlloySmelterBlockEntity> {

  public AlloySmelterBlock(BlockBehaviour.Properties properties) {
    super(BlockEntityTypeRegistry.ALLOY_SMELTER, properties);
  }

}
