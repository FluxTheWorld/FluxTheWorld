package com.fluxtheworld.core.register.block;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
public class BlockRegister extends DeferredRegister<Block> {

  public BlockRegister(String namespace) {
    super(Registries.BLOCK, namespace);
  }

  @Override
  public <B extends Block> DeferredBlock<B> register(String name, Function<ResourceLocation, ? extends B> func) {
    return (DeferredBlock<B>) super.register(name, func);
  }

  @Override
  public <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> sup) {
    return this.register(name, key -> sup.get());
  }

  @Override
  protected <I extends Block> DeferredBlock<I> createHolder(ResourceKey<? extends Registry<Block>> registryKey, ResourceLocation key) {
    return new DeferredBlock<>(ResourceKey.create(registryKey, key));
  }

  // region Utils

  // endregion

}
