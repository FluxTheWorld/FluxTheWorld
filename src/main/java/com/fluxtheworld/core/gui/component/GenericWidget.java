package com.fluxtheworld.core.gui.component;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class GenericWidget extends AbstractWidget {

  protected GenericWidget(int x, int y, int width, int height) {
    this(x, y, width, height, Component.empty());
  }

  protected GenericWidget(int x, int y, int width, int height, Component message) {
    super(x, y, width, height, message);
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
  }

}
