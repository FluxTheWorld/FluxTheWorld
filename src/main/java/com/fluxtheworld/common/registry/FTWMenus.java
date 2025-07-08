package com.fluxtheworld.common.registry;

import java.util.function.Supplier;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterMenu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FTWMenus {
  public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU,
      FTWMod.MODID);

  public static final Supplier<MenuType<AlloySmelterMenu>> ALLOY_SMELTER_MENU = MENUS
      .register("alloy_smelter_menu", () -> IMenuTypeExtension.create(AlloySmelterMenu::new));
}