package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.core.common.block.MachineBlock;
import com.fluxtheworld.core.common.registry.BlockHolder;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class AlloySmelterBlock extends MachineBlock<AlloySmelterBlockEntity> {

  public static final BlockHolder<AlloySmelterBlock> TYPE = new BlockHolder.Builder<AlloySmelterBlock>()
      .build();

  public AlloySmelterBlock(BlockBehaviour.Properties properties) {
    super(AlloySmelterBlockEntity.TYPE, properties);
  }

}
