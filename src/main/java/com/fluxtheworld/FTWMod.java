package com.fluxtheworld;

import org.slf4j.Logger;

import com.fluxtheworld.core.common.register.BlockEntityTypeRegistry;
import com.fluxtheworld.core.common.register.BlockRegistry;
import com.fluxtheworld.core.common.register.ItemRegistry;
import com.fluxtheworld.core.common.register.MenuTypeRegistry;
import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(FTWMod.MOD_ID)
public class FTWMod {

  public static final String MOD_ID = "fluxtheworld";
  public static final Logger LOGGER = LogUtils.getLogger();

  public FTWMod(FMLModContainer container, IEventBus eventBus, Dist dist) {
  }

}
