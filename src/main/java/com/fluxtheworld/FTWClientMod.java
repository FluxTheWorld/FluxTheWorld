package com.fluxtheworld;

import com.fluxtheworld.client.block.alloy_smelter.AlloySmelterScreen;
import com.fluxtheworld.core.common.register.MenuTypeRegistry;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.OnlyIns;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = FTWMod.MOD_ID, dist = Dist.CLIENT)
public class FTWClientMod {

  public FTWClientMod(FMLModContainer container, IEventBus eventBus, Dist dist) {
  }

}

