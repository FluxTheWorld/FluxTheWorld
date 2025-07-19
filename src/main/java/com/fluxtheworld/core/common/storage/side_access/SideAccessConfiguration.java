package com.fluxtheworld.core.common.storage.side_access;

import net.minecraft.core.Direction;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record SideAccessConfiguration(Map<Direction, SideAccessMode> modes) {

  public static SideAccessConfiguration copyOf(SideAccessConfiguration other) {
    return new SideAccessConfiguration(new EnumMap<>(other.modes()));
  }

  public static SideAccessConfiguration empty() {
    return new SideAccessConfiguration(new EnumMap<>(Direction.class));
  }

  public static SideAccessConfiguration of(SideAccessMode mode) {
    var config = new EnumMap<Direction, SideAccessMode>(Direction.class);
    for (Direction d : Direction.values()) {
      config.put(d, mode);
    }
    return new SideAccessConfiguration(config);
  }

  public SideAccessMode getMode(Direction side) {
    return modes.getOrDefault(side, SideAccessMode.NONE);
  }

  public SideAccessConfiguration withMode(Direction side, SideAccessMode mode) {
    Map<Direction, SideAccessMode> newModes = new EnumMap<>(Direction.class);
    newModes.putAll(modes);
    newModes.put(side, mode);
    return new SideAccessConfiguration(newModes);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SideAccessConfiguration that = (SideAccessConfiguration) o;
    return Objects.equals(modes, that.modes);
  }

  @Override
  public int hashCode() {
        int h = 0;
    for (var entry : modes.entrySet()) {
      h += entry.getKey().ordinal() ^ (31 * entry.getValue().ordinal());
    }
    return h;
  }
}