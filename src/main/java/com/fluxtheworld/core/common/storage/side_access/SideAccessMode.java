package com.fluxtheworld.core.common.storage.side_access;

import net.minecraft.util.StringRepresentable;

public enum SideAccessMode implements StringRepresentable {
  // @formatter:off
  NONE(0, "none", true, true, false, true),
  PUSH(1, "push", false, true, true, true),
  PULL(2, "pull", true, false, true, true),
  DISABLED(4,"disable", false, false, false, false);
  // @formatter:on

  public static SideAccessMode next(SideAccessMode mode) {
    switch (mode) {
      case NONE:
        return SideAccessMode.PULL;
      case PULL:
        return SideAccessMode.PUSH;
      case PUSH:
        return SideAccessMode.DISABLED;
      case DISABLED:
      default:
        return SideAccessMode.NONE;
    }
  }

  final int id;
  final String name;
  final boolean input;
  final boolean output;
  final boolean push;
  final boolean pull;
  final boolean force;
  final boolean canConnect;

  SideAccessMode(int id, String name, boolean input, boolean output, boolean force, boolean canConnect) {
    this.id = id;
    this.name = name;
    this.input = input;
    this.output = output;
    this.force = force;
    this.push = output && force;
    this.pull = input && force;
    this.canConnect = canConnect;
  }

  public SideAccessMode next() {
    return SideAccessMode.next(this);
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}