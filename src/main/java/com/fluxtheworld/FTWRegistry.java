package com.fluxtheworld;

import com.fluxtheworld.core.common.register.holder.BlockHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FTWRegistry {

  private FTWRegistry() {
  }

  private static final DeferredRegister<Block> BLOCK;

  static {
    BLOCK = DeferredRegister.create(Registries.BLOCK, FTWMod.MOD_ID);
  }

  public static void register(IEventBus eventBus, Dist dist) {
    BLOCK.register(eventBus);
  }

  private static <T extends Block> DeferredHolder<Block, T> register(BlockHolder<T> holder) {
    return BLOCK.register(holder.name(), holder.factory());
  }
}
