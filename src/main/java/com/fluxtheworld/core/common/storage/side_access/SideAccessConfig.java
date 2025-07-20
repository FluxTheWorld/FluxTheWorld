package com.fluxtheworld.core.common.storage.side_access;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.core.Direction;

public class SideAccessConfig {
  private final Map<Direction, SideAccessMode> modes;
  private final SideAccessMode defaultMode;

  public SideAccessConfig() {
    this(SideAccessMode.NONE);
  }

  public SideAccessConfig(SideAccessMode defaultMode) {
    this.modes = new EnumMap<>(Direction.class);
    this.defaultMode = defaultMode;
  }

  public SideAccessMode getMode(Direction side) {
    return this.modes.getOrDefault(side, defaultMode);
  }

  public void setMode(Direction side, SideAccessMode mode) {
    this.modes.put(side, mode);
  }
}