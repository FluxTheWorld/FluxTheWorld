package com.fluxtheworld.core.common.register.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> {

  protected DeferredItem(ResourceKey<Item> key) {
    super(key);
  }

  // region Utils

  // endregion

}