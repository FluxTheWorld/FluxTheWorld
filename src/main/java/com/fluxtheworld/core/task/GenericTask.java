package com.fluxtheworld.core.task;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface GenericTask extends INBTSerializable<CompoundTag> {
  public static GenericTask NONE = new GenericTask() {

    @Override
    public void tick() {
      // no-op
    }

    @Override
    public float getProgress() {
      return 0.0f;
    }

    @Override
    public boolean isActive() {
      return false;
    }

    @Override
    public boolean isCompleted() {
      return true;
    }

    @Override
    public CompoundTag serializeNBT(Provider provider) {
      return new CompoundTag();
    }

    @Override
    public void deserializeNBT(Provider provider, CompoundTag nbt) {
      // no-op
    }

  };

  void tick();

  float getProgress();

  boolean isActive();

  boolean isCompleted();
}
