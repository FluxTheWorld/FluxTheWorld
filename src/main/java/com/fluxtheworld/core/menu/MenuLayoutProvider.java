package com.fluxtheworld.core.menu;

public interface MenuLayoutProvider<ML extends MenuLayout> {
  public ML getClientMenuLayout();

  public ML getServerMenuLayout();
}
