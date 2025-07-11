package com.fluxtheworld.core.client.gui.screen;

import com.fluxtheworld.core.common.menu.MachineMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MachineScreen<T extends MachineMenu<?>> extends GenericContainerScreen<T> {

  public MachineScreen(T menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

}
