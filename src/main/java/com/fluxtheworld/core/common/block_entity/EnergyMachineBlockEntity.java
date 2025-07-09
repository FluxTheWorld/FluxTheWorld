package com.fluxtheworld.core.common.block_entity;

import com.fluxtheworld.core.common.storage.EnergyStorage;
import com.fluxtheworld.core.common.storage.GenericEnergyStorage;
import com.ibm.icu.text.RelativeDateTimeFormatter.Direction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class EnergyMachineBlockEntity extends MachineBlockEntity {

  // TODO: Add side io control and move to class
  public static final ICapabilityProvider<EnergyMachineBlockEntity, Direction, IEnergyStorage> ENERGY_STORAGE_PROVIDER = (
      be, side) -> be.energyStorage;

  private final EnergyStorage energyStorage;

  public EnergyMachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
    this.energyStorage = new GenericEnergyStorage(512_000, 1024, 1024, 256_000);
  }

  // region Serialization

  @Override
  protected void saveAdditional(CompoundTag tag, Provider registries) {
    super.saveAdditional(tag, registries);
    tag.put("Energy", this.energyStorage.serializeNBT(registries));
  }

  @Override
  protected void loadAdditional(CompoundTag tag, Provider registries) {
    super.loadAdditional(tag, registries);
    if (tag.contains("Energy")) {
      energyStorage.deserializeNBT(registries, tag);
    }
  }

  // endregion
}
