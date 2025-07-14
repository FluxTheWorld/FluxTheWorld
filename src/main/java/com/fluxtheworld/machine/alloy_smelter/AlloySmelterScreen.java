package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterMenu;
import com.fluxtheworld.core.client.gui.screen.MachineScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterScreen extends MachineScreen<AlloySmelterMenu> {

  public AlloySmelterScreen(AlloySmelterMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
  }

}
