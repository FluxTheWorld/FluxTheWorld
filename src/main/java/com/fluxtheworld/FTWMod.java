package com.fluxtheworld;

import org.slf4j.Logger;

import com.fluxtheworld.core.common.register.ClientRegister;
import com.fluxtheworld.core.common.register.CommonRegister;
import com.fluxtheworld.core.common.register.ServerRegister;
import com.fluxtheworld.machine.alloy_smelter.AlloySmelterRegistry;
import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FTWMod.MOD_ID)
public class FTWMod {

  public static final String MOD_ID = "fluxtheworld";
  public static final Logger LOGGER = LogUtils.getLogger();

  public FTWMod(IEventBus eventBus, Dist dist) {
    CommonRegister register = dist.isClient() ? new ClientRegister(MOD_ID) : new ServerRegister(MOD_ID);
    register.register(eventBus);

    AlloySmelterRegistry.register(register, dist);
  }

}
