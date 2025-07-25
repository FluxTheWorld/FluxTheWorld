package com.fluxtheworld.core.gui.component;

import org.apache.commons.lang3.NotImplementedException;

import com.fluxtheworld.core.util.FloatSupplier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ProgressWidget extends GenericWidget {

  public enum FlowDirection {
    BOTTOM_UP, TOP_DOWN, LEFT_RIGHT,
  }

  private final ResourceLocation sprite;
  private final FlowDirection flowDirection;
  private final FloatSupplier progressSupplier;

  private ProgressWidget(int x, int y, int width, int height,
      ResourceLocation sprite, FlowDirection flowDirection, FloatSupplier progressSupplier) {
    super(x, y, width, height);
    this.flowDirection = flowDirection;
    this.sprite = sprite;
    this.progressSupplier = progressSupplier;
  }

  public static ProgressWidget bottomUp(int x, int y, int width, int height, ResourceLocation sprite, FloatSupplier progressSupplier) {
    return new ProgressWidget(x, y, width, height, sprite, FlowDirection.BOTTOM_UP, progressSupplier);
  }

  public static ProgressWidget topDown(int x, int y, int width, int height, ResourceLocation sprite, FloatSupplier progressSupplier) {
    return new ProgressWidget(x, y, width, height, sprite, FlowDirection.TOP_DOWN, progressSupplier);
  }

  public static ProgressWidget leftRight(int x, int y, int width, int height, ResourceLocation sprite, FloatSupplier progressSupplier) {
    return new ProgressWidget(x, y, width, height, sprite, FlowDirection.LEFT_RIGHT, progressSupplier);
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    float progress = progressSupplier.getAsFloat();

    int u = 0;
    int v = 0;
    int x;
    int y;
    int uWidth;
    int vHeight;

    switch (flowDirection) {
      case BOTTOM_UP -> {
        int yOffset = (int) (this.height * (1.0f - progress));
        x = getX();
        y = getY() + yOffset;
        v = yOffset;
        uWidth = width;
        vHeight = (int) (this.height * progress);
      }
      case TOP_DOWN -> {
        x = getX();
        y = getY();
        uWidth = width;
        vHeight = (int) (this.height * progress);
      }
      case LEFT_RIGHT -> {
        x = getX();
        y = getY();
        uWidth = (int) (this.width * progress);
        vHeight = height;
      }
      default -> throw new NotImplementedException();
    }

    guiGraphics.blitSprite(sprite, width, height, u, v, x, y, uWidth, vHeight);
  }
}
