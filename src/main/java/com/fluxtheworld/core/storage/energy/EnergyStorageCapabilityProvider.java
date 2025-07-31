package com.fluxtheworld.core.storage.energy;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyStorageCapabilityProvider implements ICapabilityProvider<BlockEntity, @Nullable Direction, IEnergyStorage> {

  @Override
  public @Nullable IEnergyStorage getCapability(BlockEntity be, @Nullable Direction side) {
    if (be instanceof EnergyStorageProvider provider) {
      return provider.getEnergyStorage().getForPipe();
    }
    return null;
  }

  public interface EnergyStorageProvider {
    EnergyStorage getEnergyStorage();
  }
}