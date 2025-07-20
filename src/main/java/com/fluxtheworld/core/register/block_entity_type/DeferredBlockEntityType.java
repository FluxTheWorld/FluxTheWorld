package com.fluxtheworld.core.register.block_entity_type;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredBlockEntityType<T extends BlockEntityType<?>> extends DeferredHolder<BlockEntityType<?>, T> {

  protected DeferredBlockEntityType(ResourceKey<BlockEntityType<?>> key) {
    super(key);
  }

  // region Utils

  // endregion
}