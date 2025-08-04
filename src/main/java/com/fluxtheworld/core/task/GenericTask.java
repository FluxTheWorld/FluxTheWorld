package com.fluxtheworld.core.task;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface GenericTask extends INBTSerializable<CompoundTag> {
  public static GenericTask NONE = new GenericTask() {
    
    @Override
    public TaskState tick() {
      return TaskState.Completed;
    }

    public float getProgress() {
      return 0.0f;
    }

    @Override
    public TaskState getState() {
      return TaskState.Completed;
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

  TaskState tick();

  float getProgress();

  TaskState getState();
}
