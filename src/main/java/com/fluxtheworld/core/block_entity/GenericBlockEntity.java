package com.fluxtheworld.core.block_entity;

import java.util.HashSet;
import java.util.Set;

import com.fluxtheworld.core.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.LogicalSide;

public class GenericBlockEntity extends BlockEntity {
  private Set<BlockEntityChange> changes;

  public GenericBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
    this.changes = new HashSet<>();
  }

  // region Ticking

  public static void tick(Level level, BlockPos pos, BlockState state, GenericBlockEntity blockEntity) {
    if (level.isClientSide) {
      blockEntity.clientTick();
    }
    else {
      blockEntity.serverTick();
    }
    blockEntity.tick();
  }

  public void serverTick() {
    Preconditions.ensureSide(LogicalSide.SERVER);
  }

  public void clientTick() {
    Preconditions.ensureSide(LogicalSide.CLIENT);
  }

  public void tick() {
    this.changes.clear();
  }

  // endregion

  // region Changes

  public void setChanged(BlockEntityChange change) {
    this.changes.add(change);
    this.setChanged();
  }

  public boolean wasChanged(BlockEntityChange change) {
    return this.changes.contains(change);
  }

  // endregion

  // region Serialization

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag data = super.getUpdateTag(registries);
    this.saveAdditionalSynced(data, registries);
    return data;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    this.saveAdditionalSynced(tag, registries);
  }

  protected void saveAdditionalSynced(CompoundTag tag, HolderLookup.Provider registries) {
  }

  // endregion
}
