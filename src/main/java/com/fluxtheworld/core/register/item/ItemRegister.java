package com.fluxtheworld.core.register.item;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
public class ItemRegister extends DeferredRegister<Item> {

  public ItemRegister(String namespace) {
    super(Registries.ITEM, namespace);
  }

  @Override
  public <I extends Item> DeferredItem<I> register(String name, Function<ResourceLocation, ? extends I> func) {
    return (DeferredItem<I>) super.register(name, func);
  }

  @Override
  public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> sup) {
    return this.register(name, key -> sup.get());
  }

  @Override
  protected <I extends Item> DeferredItem<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key) {
    return new DeferredItem<>(ResourceKey.create(registryKey, key));
  }

  // region Utils

  // endregion

}