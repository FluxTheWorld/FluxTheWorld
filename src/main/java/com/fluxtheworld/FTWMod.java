package com.fluxtheworld;

import org.slf4j.Logger;

import com.fluxtheworld.common.registry.FTWMenus;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(FTWMod.MODID)
public class FTWMod {
  public static final String MODID = "fluxtheworld";
  public static final Logger LOGGER = LogUtils.getLogger();

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
      .create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
      .register("example_tab", () -> CreativeModeTab.builder()
          .title(Component.translatable("itemGroup.fluxtheworld"))
          .build());

  public FTWMod(IEventBus modEventBus, ModContainer modContainer) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);
    FTWMenus.MENUS.register(modEventBus);
  }
}
