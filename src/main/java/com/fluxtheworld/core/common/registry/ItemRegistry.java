package com.fluxtheworld.core.common.registry;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.common.registry.holder.ItemHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

  private ItemRegistry() {
  }

  private static final DeferredRegister<Item> ITEM;

  static {
    ITEM = DeferredRegister.create(Registries.ITEM, FTWMod.MOD_ID);
  }

  public static void register(IEventBus modEventBus) {
    ITEM.register(modEventBus);
  }

  private static <T extends Item> DeferredHolder<Item, T> register(ItemHolder<T> holder) {
    return ITEM.register(holder.name(), holder.factory());
  }

  // --- --- ---

  public static final DeferredHolder<Item, BlockItem> MY_BETTER_BLOCK_ITEM = register(
      new ItemHolder.Builder<BlockItem>()
          .name("alloy_smelter")
          .factory(() -> new BlockItem(BlockRegistry.ALLOY_SMELTER_BLOCK.get(), new Item.Properties()))
          .build());
}
