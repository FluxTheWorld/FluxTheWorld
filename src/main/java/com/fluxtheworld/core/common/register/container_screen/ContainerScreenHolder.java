package com.fluxtheworld.core.common.register.container_screen;

import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ContainerScreenHolder<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> {

  private final MenuType<M> menuType;
  private final ScreenConstructor<M, S> screenConstructor;

  public ContainerScreenHolder(MenuType<M> menuType, ScreenConstructor<M, S> screenConstructor) {
    this.menuType = menuType;
    this.screenConstructor = screenConstructor;
  }

  public MenuType<M> getMenuType() {
    return menuType;
  }

  public ScreenConstructor<M, S> getScreenConstructor() {
    return screenConstructor;
  }

  public static class Builder<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> {

    private @Nullable MenuType<M> menuType;
    private @Nullable ScreenConstructor<M, S> screenConstructor;

    public Builder<M, S> menuType(MenuType<M> menuType) {
      this.menuType = menuType;
      return this;
    }

    public Builder<M, S> screenConstructor(ScreenConstructor<M, S> screenConstructor) {
      this.screenConstructor = screenConstructor;
      return this;
    }

    @SuppressWarnings("null")
    public ContainerScreenHolder<M, S> build() {
      Objects.requireNonNull(this.menuType, "menuType is null");
      Objects.requireNonNull(this.screenConstructor, "screenConstructor is null");
      return new ContainerScreenHolder<>(this.menuType, this.screenConstructor);
    }
  }
}