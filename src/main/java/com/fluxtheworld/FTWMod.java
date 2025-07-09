package com.fluxtheworld;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(FTWMod.MODID)
public class FTWMod {
  public static final String MODID = "fluxtheworld";
  public static final Logger LOGGER = LogUtils.getLogger();


  public FTWMod(IEventBus modEventBus, ModContainer modContainer) {
  }
}
