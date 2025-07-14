package com.fluxtheworld.client;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.client.block.alloy_smelter.AlloySmelterScreen;
import com.fluxtheworld.registry.MenuTypeRegistry;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.OnlyIns;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIns
@EventBusSubscriber(modid = FTWMod.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

  @SubscribeEvent
  public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(MenuTypeRegistry.ALLOY_SMELTER.get(), AlloySmelterScreen::new);
  }

}
