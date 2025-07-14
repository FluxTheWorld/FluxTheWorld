package com.fluxtheworld.core.common.registry.holder;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;

public class MenuTypeHolder<M extends AbstractContainerMenu> {

  private final String name;
  private final Supplier<MenuType<M>> factory;

  public MenuTypeHolder(String name, Supplier<MenuType<M>> factory) {
    this.name = name;
    this.factory = factory;
  }

  public String name() {
    return this.name;
  }

  public Supplier<MenuType<M>> factory() {
    return this.factory;
  }

  public static class Builder<M extends AbstractContainerMenu> {

    private @Nullable String name;
    private @Nullable IContainerFactory<M> factory;

    public Builder() {
    }

    public Builder<M> name(String name) {
      this.name = name;
      return this;
    }

    public Builder<M> factory(IContainerFactory<M> factory) {
      this.factory = factory;
      return this;
    }

    @SuppressWarnings("null")
    public MenuTypeHolder<M> build() {
      Preconditions.checkNotNull(this.name, "name is null");
      Preconditions.checkNotNull(this.factory, "factory is null");
      return new MenuTypeHolder<>(this.name, () -> IMenuTypeExtension.create(this.factory));
    }

  }
}
