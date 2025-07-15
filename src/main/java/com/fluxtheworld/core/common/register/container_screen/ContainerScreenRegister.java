package com.fluxtheworld.core.common.register.container_screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ContainerScreenRegister {

  private final List<ContainerScreenHolder<? extends AbstractContainerMenu, ? extends AbstractContainerScreen<?>>> holders = new ArrayList<>();

  public ContainerScreenRegister(String namespace) {
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void register(IEventBus eventBus) {
    eventBus.addListener(RegisterMenuScreensEvent.class, event -> {
      for (ContainerScreenHolder holder : holders) {
        event.register(holder.getMenuType(), holder.getScreenConstructor());
      }
    });
  }

  public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(ContainerScreenHolder<M, S> screenHolder) {
    this.holders.add(screenHolder);
  }

  public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(
      MenuType<M> menuType,
      ScreenConstructor<M, S> screenConstructor) {
    this.register(new ContainerScreenHolder<>(menuType, screenConstructor));
  }
}