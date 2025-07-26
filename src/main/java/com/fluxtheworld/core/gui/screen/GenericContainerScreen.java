package com.fluxtheworld.core.gui.screen;

import javax.annotation.Nullable;

import com.fluxtheworld.FTW;
import com.fluxtheworld.core.gui.component.BoxLayout;
import com.fluxtheworld.core.gui.component.GenericWidget;
import com.fluxtheworld.core.menu.GenericMenu;
import com.fluxtheworld.core.menu.MenuLayout;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class GenericContainerScreen<T extends GenericMenu> extends AbstractContainerScreen<T> {

  private static final ResourceLocation BG = FTW.loc("bg");

  private @Nullable BoxLayout box;

  protected GenericContainerScreen(T menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    this.box = null;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blitSprite(BG, leftPos, topPos, imageWidth, imageHeight);
  }

  // region MenuLayout

  @SuppressWarnings("null")
  protected void applyLayout(MenuLayout layout) {
    if (this.box == null) {
      this.box = new BoxLayout(0, 0).addChildren(layout.getWidgets()).addChildren(layout.getRenderables());
    }

    for (GenericWidget widget : layout.getWidgets()) {
      this.addWidget(widget);
    }

    for (GenericWidget widget : layout.getRenderables()) {
      this.addRenderableOnly(widget);
    }

    this.box.setX(this.leftPos);
    this.box.setY(this.topPos);
    this.box.invalidate();
  }

  // endregion
}
