package com.fluxtheworld.core.common;

import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.util.thread.EffectiveSide;

public class Preconditions {
  private Preconditions() {
  }

  public static void ensureSide(LogicalSide side) {
    LogicalSide current = EffectiveSide.get();
    if (side != current) {
      String message = String.format("Method should be called on %s but was called on %s",
          side.toString(), current.toString());
      throw new IllegalStateException(message);
    }
  }
}
