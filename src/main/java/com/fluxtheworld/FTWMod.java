package com.fluxtheworld;

import org.slf4j.Logger;

import com.fluxtheworld.core.register.ClientRegister;
import com.fluxtheworld.core.register.CommonRegister;
import com.fluxtheworld.core.register.ServerRegister;
import com.fluxtheworld.machine.alloy_smelter.AlloySmelterRegistry;
import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FTW.NAMESPACE)
public class FTWMod {

  public static final Logger LOGGER = LogUtils.getLogger();

  public FTWMod(IEventBus eventBus, Dist dist) {
    CommonRegister register = dist.isClient() ? new ClientRegister(FTW.NAMESPACE) : new ServerRegister(FTW.NAMESPACE);
    register.register(eventBus);

    AlloySmelterRegistry.register(register, dist);
  }

}
