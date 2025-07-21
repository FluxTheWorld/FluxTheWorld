package com.fluxtheworld;

import net.minecraft.resources.ResourceLocation;

public class FTW {
  public static final String NAMESPACE = "fluxtheworld";

  public static ResourceLocation loc(String path) {
    return ResourceLocation.fromNamespaceAndPath(NAMESPACE, path);
  }
}
