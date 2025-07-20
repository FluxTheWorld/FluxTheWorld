package com.fluxtheworld.core.register;

import com.fluxtheworld.core.Preconditions;
import com.fluxtheworld.core.register.container_screen.ContainerScreenRegister;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.LogicalSide;

public class ClientRegister extends CommonRegister {

  public final ContainerScreenRegister containerScreen;

  public ClientRegister(String namespace) {
    super(namespace);
    Preconditions.ensureSide(LogicalSide.CLIENT);
    this.containerScreen = new ContainerScreenRegister(namespace);
  }

  @Override
  public void register(IEventBus eventBus) {
    super.register(eventBus);
    this.containerScreen.register(eventBus);
  }

}
