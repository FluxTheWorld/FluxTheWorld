package com.fluxtheworld.core.block_entity;

// Maybe later it will be a enum or sealed interface with some args
public interface BlockEntityChange {

  public static BlockEntityChange ItemStorage = new BlockEntityChange() {
  };

  public static BlockEntityChange FluidStorage = new BlockEntityChange() {
  };

  public static BlockEntityChange EnergyStorage = new BlockEntityChange() {
  };

}