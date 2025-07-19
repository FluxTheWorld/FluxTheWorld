package com.fluxtheworld.core.common.storage.side_access;

import java.util.List;

import net.minecraft.core.Direction;

public class MachineSideAccessController implements MutableSideAccessConfigurable {

  private SideAccessConfiguration config;

  public MachineSideAccessController() {
    this.config = SideAccessConfiguration.of(SideAccessMode.NONE);
  }

  @Override
  public SideAccessMode getMode(Direction side) {
    return config.getMode(side);
  }

  @Override
  public void setMode(Direction side, SideAccessMode mode) {
    if (!this.isModeSupported(side, mode))
      return;

    this.config = this.config.withMode(side, mode);
  }

  @Override
  public boolean isModeSupported(Direction side, SideAccessMode state) {
    return true;
  }

  @Override
  public void setNextMode(Direction side) {
    SideAccessMode currentMode = this.getMode(side);
    this.setMode(side, currentMode.next());
  }
}