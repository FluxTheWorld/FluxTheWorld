package com.fluxtheworld.core.register.menu_type;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unchecked")
public class MenuTypeRegister extends DeferredRegister<MenuType<?>> {

  public MenuTypeRegister(String namespace) {
    super(Registries.MENU, namespace);
  }

  @Override
  public <T extends MenuType<?>> DeferredMenuType<T> register(String name, Function<ResourceLocation, ? extends T> func) {
    return (DeferredMenuType<T>) super.register(name, func);
  }

  @Override
  public <T extends MenuType<?>> DeferredMenuType<T> register(String name, Supplier<? extends T> sup) {
    return this.register(name, key -> sup.get());
  }

  @Override
  protected <T extends MenuType<?>> DeferredMenuType<T> createHolder(ResourceKey<? extends Registry<MenuType<?>>> registryKey, ResourceLocation key) {
    return new DeferredMenuType<>(ResourceKey.create(registryKey, key));
  }

  // region Utils

  // endregion

}