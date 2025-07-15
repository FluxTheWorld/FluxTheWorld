package com.fluxtheworld.core.common.register.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

// DeferredScreen will hold the MenuType and its associated ScreenFactory
public class DeferredScreen<M extends AbstractContainerMenu, S extends Screen> {

    private final MenuType<M> menuType;
    private final ScreenRegister.ScreenFactory<M, S> screenFactory;

    public DeferredScreen(MenuType<M> menuType, ScreenRegister.ScreenFactory<M, S> screenFactory) {
        this.menuType = menuType;
        this.screenFactory = screenFactory;
    }

    public MenuType<M> getMenuType() {
        return menuType;
    }

    public ScreenRegister.ScreenFactory<M, S> getScreenFactory() {
        return screenFactory;
    }

    public S createScreen(M menu, Inventory inventory, Component title) {
        return screenFactory.create(menu, inventory, title);
    }
}