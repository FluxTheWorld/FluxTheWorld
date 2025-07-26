package com.fluxtheworld.core.gui.component;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.components.AbstractWidget;

public class BoxLayout {
  private final List<ChildWrapper> children;
  private final Settings settings;

  public BoxLayout(int x, int y) {
    this.children = new ArrayList<>();
    this.settings = new Settings(x, y);
  }

  public void setX(int x) {
    this.settings.x = x;
  }

  public void setY(int y) {
    this.settings.y = y;
  }

  public BoxLayout addChild(AbstractWidget child) {
    this.children.add(new ChildWrapper(child, this.settings));
    return this;
  }

  public <T extends AbstractWidget> BoxLayout addChildren(List<T> children) {
    for (AbstractWidget child : children) {
      this.addChild(child);
    }
    return this;
  }

  public void invalidate() {
    for (ChildWrapper wrapper : this.children) {
      wrapper.invalidate();
    }
  }

  public void removeAllChildren() {
    this.children.clear();
  }

  static class Settings {
    public int x;
    public int y;

    public Settings(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  static class ChildWrapper {
    private final int initialX;
    private final int initialY;
    private final AbstractWidget widget;
    private final Settings settings;

    public ChildWrapper(AbstractWidget widget, Settings settings) {
      this.initialX = widget.getX();
      this.initialY = widget.getY();
      this.widget = widget;
      this.settings = settings;
    }

    public void invalidate() {
      this.widget.setX(initialX + settings.x);
      this.widget.setY(initialY + settings.y);
    }
  }
}
