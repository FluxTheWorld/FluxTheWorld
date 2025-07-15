package com.fluxtheworld.core.common.register.block_entity_type;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
public class BlockEntityTypeRegister extends DeferredRegister<BlockEntityType<?>> {

  public BlockEntityTypeRegister(String namespace) {
    super(Registries.BLOCK_ENTITY_TYPE, namespace);
  }

  @Override
  public <T extends BlockEntityType<?>> DeferredBlockEntityType<T> register(String name, Function<ResourceLocation, ? extends T> func) {
    return (DeferredBlockEntityType<T>) super.register(name, func);
  }

  @Override
  public <T extends BlockEntityType<?>> DeferredBlockEntityType<T> register(String name, Supplier<? extends T> sup) {
    return this.register(name, key -> sup.get());
  }

  @Override
  protected <T extends BlockEntityType<?>> DeferredBlockEntityType<T> createHolder(ResourceKey<? extends Registry<BlockEntityType<?>>> registryKey, ResourceLocation key) {
    return new DeferredBlockEntityType<>(ResourceKey.create(registryKey, key));
  }

  // region Utils

  // endregion

}