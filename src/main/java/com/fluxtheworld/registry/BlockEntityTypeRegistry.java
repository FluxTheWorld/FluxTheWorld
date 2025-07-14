package com.fluxtheworld.registry;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlockEntity;
import com.fluxtheworld.registry.holder.BlockEntityTypeHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityTypeRegistry {

  private BlockEntityTypeRegistry() {
  }

  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE;

  static {
    BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FTWMod.MOD_ID);
  }

  public static void register(IEventBus modEventBus) {
    BLOCK_ENTITY_TYPE.register(modEventBus);
  }

  private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(BlockEntityTypeHolder<T> holder) {
    return BLOCK_ENTITY_TYPE.register(holder.name(), holder.factory());
  }

  // --- --- ---

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlloySmelterBlockEntity>> ALLOY_SMELTER = register(
      new BlockEntityTypeHolder.Builder<AlloySmelterBlockEntity>()
          .name("alloy_smelter")
          .factory(AlloySmelterBlockEntity::new)
          .blocks(BlockRegistry.ALLOY_SMELTER_BLOCK)
          .build());

}
