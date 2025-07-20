package com.fluxtheworld.core.register.container_screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ContainerScreenRegister {

  private final List<Supplier<?>> holders = new ArrayList<>();

  public ContainerScreenRegister(String namespace) {
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void register(IEventBus eventBus) {
    eventBus.addListener(RegisterMenuScreensEvent.class, event -> {
      for (Supplier supplier : holders) {
        ContainerScreenHolder holder = (ContainerScreenHolder) supplier.get();
        event.register(holder.getMenuType(), holder.getScreenConstructor());
      }
    });
  }

  public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<ContainerScreenHolder<M, S>> sup) {
    this.holders.add(sup);
  }
}