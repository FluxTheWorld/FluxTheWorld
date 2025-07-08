package com.fluxtheworld.common.registry;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FTWBlocks {

  private FTWBlocks() {
  }

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FTWMod.MODID);

  public static final DeferredBlock<AlloySmelterBlock> ALLOY_SMELTER = BLOCKS.registerBlock(
      "alloy_smelter",
      AlloySmelterBlock::new,
      BlockBehaviour.Properties.of());
}