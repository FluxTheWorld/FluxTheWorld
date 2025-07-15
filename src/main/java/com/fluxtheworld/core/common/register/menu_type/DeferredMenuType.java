package com.fluxtheworld.core.common.register.menu_type;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredMenuType<T extends MenuType<?>> extends DeferredHolder<MenuType<?>, T> {

  protected DeferredMenuType(ResourceKey<MenuType<?>> key) {
    super(key);
  }

  // region Utils

  // endregion
}