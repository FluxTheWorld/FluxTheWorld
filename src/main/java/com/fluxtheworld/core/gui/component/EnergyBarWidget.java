package com.fluxtheworld.core.gui.component;

import com.fluxtheworld.FTW;
import com.fluxtheworld.core.util.FloatSupplier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class EnergyBarWidget extends GenericWidget {

  private static final ResourceLocation BG = FTW.loc("energy_bar_bg");
  private static final ResourceLocation FG = FTW.loc("energy_bar_fg");

  private final FloatSupplier progressSupplier;

  private EnergyBarWidget(int x, int y, int width, int height, FloatSupplier progressSupplier) {
    super(x, y, width, height);
    this.progressSupplier = progressSupplier;
  }

  public static EnergyBarWidget defaultSize(int x, int y, FloatSupplier progressSupplier) {
    return new EnergyBarWidget(x, y, 8, 56, progressSupplier);
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    float progress = progressSupplier.getAsFloat();

    final var x = this.getX();
    final var y = this.getY();
    final var width = this.getWidth();
    final var height = this.getHeight();

    final var scaled = (int) (height * progress);

    guiGraphics.blitSprite(BG, x, y, width, height);
    guiGraphics.blitSprite(FG, x + 2, y + scaled - 2, width - 4, scaled);
  }
}
