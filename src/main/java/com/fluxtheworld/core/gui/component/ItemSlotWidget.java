package com.fluxtheworld.core.gui.component;

import com.fluxtheworld.FTW;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ItemSlotWidget extends GenericWidget {

  private static final ResourceLocation BG = FTW.loc("slot");

  public ItemSlotWidget(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    guiGraphics.blitSprite(BG, this.getX(), this.getY(), this.width, this.height);
  }

}
