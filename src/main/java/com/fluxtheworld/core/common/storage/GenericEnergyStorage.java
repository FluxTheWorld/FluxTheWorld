package com.fluxtheworld.core.common.storage;

public class GenericEnergyStorage extends net.neoforged.neoforge.energy.EnergyStorage implements EnergyStorage {

  public GenericEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
    super(capacity, maxReceive, maxExtract, energy);
  }

}
