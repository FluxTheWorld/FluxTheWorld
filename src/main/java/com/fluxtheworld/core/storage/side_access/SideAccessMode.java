package com.fluxtheworld.core.storage.side_access;

import net.minecraft.util.StringRepresentable;

public enum SideAccessMode implements StringRepresentable {
  // @formatter:off
  NONE("none", true, true, false, true),
  PUSH("push", false, true, true, true),
  PULL("pull", true, false, true, true),
  DISABLED("disable", false, false, false, false);
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

  private final String name;
  private final boolean input;
  private final boolean output;
  private final boolean force;
  private final boolean canConnect;

  SideAccessMode(String name, boolean input, boolean output, boolean force, boolean canConnect) {
    this.name = name;
    this.input = input;
    this.output = output;
    this.force = force;
    this.canConnect = canConnect;
  }

  public boolean canInput() {
    return input;
  }

  public boolean canOutput() {
    return output;
  }

  public boolean canConnect() {
    return canConnect;
  }

  public boolean canPush() {
    return canOutput() && canForce();
  }

  public boolean canPull() {
    return canInput() && canForce();
  }

  public boolean canForce() {
    return force;
  }

  public SideAccessMode next() {
    return SideAccessMode.next(this);
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}