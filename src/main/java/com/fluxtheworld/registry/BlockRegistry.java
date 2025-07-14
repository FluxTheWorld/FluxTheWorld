package com.fluxtheworld.registry;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlock;
import com.fluxtheworld.registry.holder.BlockHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

  private BlockRegistry() {
  }

  private static final DeferredRegister<Block> BLOCK;

  static {
    BLOCK = DeferredRegister.create(Registries.BLOCK, FTWMod.MOD_ID);
  }

  public static void register(IEventBus modEventBus) {
    BLOCK.register(modEventBus);
  }

  private static <T extends Block> DeferredHolder<Block, T> register(BlockHolder<T> holder) {
    return BLOCK.register(holder.name(), holder.factory());
  }

  // --- --- ---

  public static final DeferredHolder<Block, AlloySmelterBlock> ALLOY_SMELTER_BLOCK = register(
      new BlockHolder.Builder<AlloySmelterBlock>()
          .name("alloy_smelter")
          .factory(() -> new AlloySmelterBlock(BlockBehaviour.Properties.of()))
          .build());

}
