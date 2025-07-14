package com.fluxtheworld.registry;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterMenu;
import com.fluxtheworld.registry.holder.MenuTypeHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypeRegistry {

  private MenuTypeRegistry() {
  }

  private static final DeferredRegister<MenuType<?>> MENU_TYPE;

  static {
    MENU_TYPE = DeferredRegister.create(Registries.MENU, FTWMod.MOD_ID);
  }

  public static void register(IEventBus modEventBus) {
    MENU_TYPE.register(modEventBus);
  }

  private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(MenuTypeHolder<T> holder) {
    return MENU_TYPE.register(holder.name(), holder.factory());
  }

  // --- --- ---

  public static final DeferredHolder<MenuType<?>, MenuType<AlloySmelterMenu>> ALLOY_SMELTER = register(
      new MenuTypeHolder.Builder<AlloySmelterMenu>()
          .name("alloy_smelter")
          .factory(AlloySmelterMenu::new)
          .build());

}
