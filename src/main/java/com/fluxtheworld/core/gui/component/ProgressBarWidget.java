package com.fluxtheworld.core.gui.component;

import org.apache.commons.lang3.NotImplementedException;

import com.fluxtheworld.FTW;
import com.fluxtheworld.core.util.FloatSupplier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ProgressBarWidget extends GenericWidget {

  private static final ResourceLocation ARROW_RIGHT = FTW.loc("arrow_right");
  private static final ResourceLocation ARROW_RIGHT_ON = FTW.loc("arrow_right_on");

  public enum Direction {
    UP, RIGHT,
  }

  private final ResourceLocation spriteBg;
  private final ResourceLocation spriteOn;
  private final Direction flowDirection;
  private final FloatSupplier progressSupplier;

  @SuppressWarnings("java:S107")
  private ProgressBarWidget(int x, int y, int width, int height,
      ResourceLocation spriteBg, ResourceLocation spriteOn, Direction flowDirection, FloatSupplier progressSupplier) {
    super(x, y, width, height);
    this.flowDirection = flowDirection;
    this.spriteBg = spriteBg;
    this.spriteOn = spriteOn;
    this.progressSupplier = progressSupplier;
  }

  public static ProgressBarWidget arrowRight(int x, int y, FloatSupplier progressSupplier) {
    return new ProgressBarWidget(x, y, 24, 16, ARROW_RIGHT, ARROW_RIGHT_ON, Direction.RIGHT, progressSupplier);
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
      case UP -> {
        int yOffset = (int) (this.getHeight() * (1.0f - progress));
        x = this.getX();
        y = this.getY() + yOffset;
        v = yOffset;
        uWidth = width;
        vHeight = (int) (this.getHeight() * progress);
      }
      case RIGHT -> {
        x = getX();
        y = getY();
        uWidth = (int) (this.getWidth() * progress);
        vHeight = height;
      }
      default -> throw new NotImplementedException();
    }

    guiGraphics.blitSprite(spriteBg, x, y, width, height);
    guiGraphics.blitSprite(spriteOn, width, height, u, v, x, y, uWidth, vHeight);
  }
}
