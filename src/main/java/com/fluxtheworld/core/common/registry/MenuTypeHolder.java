package com.fluxtheworld.core.common.registry;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MenuTypeHolder<M extends AbstractContainerMenu> implements Supplier<MenuType<M>> {

  private final String name;
  private final Supplier<MenuType<M>> factory;

  public MenuTypeHolder(String name, Supplier<MenuType<M>> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  @Override
  public MenuType<M> get() {
    return this.factory.get();
  }

  public static class Builder<M extends AbstractContainerMenu> {

    private @Nullable String name;
    private @Nullable Supplier<MenuType<M>> factory;

    public Builder() {
    }

    public Builder<M> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<M> factory(Supplier<MenuType<M>> factory) {
      this.factory = factory;
      return this;
    }

    public MenuTypeHolder<M> build() {
      Preconditions.checkNotNull(this.name, "name is null");
      Preconditions.checkNotNull(this.factory, "factory is null");
      return new MenuTypeHolder<>(name, factory);
    }

  }
}
