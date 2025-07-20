package com.fluxtheworld.core.gui.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GenericContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

  public GenericContainerScreen(T menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

}
