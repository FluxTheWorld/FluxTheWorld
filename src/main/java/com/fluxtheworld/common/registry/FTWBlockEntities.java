package com.fluxtheworld.common.registry;

import java.util.function.Supplier;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlockEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FTWBlockEntities {

  private FTWBlockEntities() {
  }

  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
      BuiltInRegistries.BLOCK_ENTITY_TYPE,
      FTWMod.MODID);

  public static final Supplier<BlockEntityType<AlloySmelterBlockEntity>> MY_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
      "alloy_smelter_entity",
      () -> BlockEntityType.Builder.of(
          AlloySmelterBlockEntity::new,
          FTWBlocks.ALLOY_SMELTER.get()).build(null));

}