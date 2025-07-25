package com.fluxtheworld.core.gui.screen;

import com.fluxtheworld.core.menu.MachineMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MachineScreen<T extends MachineMenu<?, ?>> extends GenericContainerScreen<T> {

  protected MachineScreen(T menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

  @Override
  protected void init() {
    super.init();
    this.applyLayout(this.menu.getClientMenuLayout());
  }

}
