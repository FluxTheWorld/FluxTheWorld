package com.fluxtheworld.core.storage.energy;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag> {

  protected int energy;
  protected int capacity;

  public EnergyStorage(int capacity) {
    this.capacity = capacity;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    int energyReceived = Math.min(this.capacity - this.energy, maxReceive);
    if (!simulate) {
      this.energy += energyReceived;
    }
    return energyReceived;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    int energyExtracted = Math.min(this.energy, maxExtract);
    if (!simulate) {
      this.energy -= energyExtracted;
    }
    return energyExtracted;
  }

  @Override
  public int getEnergyStored() {
    return energy;
  }

  @Override
  public int getMaxEnergyStored() {
    return capacity;
  }

  @Override
  public boolean canExtract() {
    return true;
  }

  @Override
  public boolean canReceive() {
    return true;
  }

  public EnergyStorage getForPipe() {
    return this;
  }

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    CompoundTag tag = new CompoundTag();
    tag.putInt("Energy", this.energy);
    return tag;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
    this.energy = tag.getInt("Energy");
  }
}