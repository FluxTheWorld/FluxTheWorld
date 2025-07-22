package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.FTW;
import com.fluxtheworld.core.gui.screen.MachineScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterScreen extends MachineScreen<AlloySmelterMenu> {

  private static final ResourceLocation BG_TEXTURE = FTW.loc("textures/gui/screen/empty.png");

  public AlloySmelterScreen(AlloySmelterMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);

    this.imageWidth = 176;
    this.imageHeight = 166;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blit(BG_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

}
