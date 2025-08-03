package com.fluxtheworld.core.task;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface GenericTask extends INBTSerializable<CompoundTag> {
  public static GenericTask NONE = new GenericTask() {
    private final TaskState state = new TaskState.Completed();

    @Override
    public void tick() {
      // no-op
    }

    public float getProgress() {
      return 0.0f;
    }

    @Override
    public TaskState getState() {
      return this.state;
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

  TaskState getState();
}
