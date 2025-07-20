package com.fluxtheworld.core.task;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface MachineTask extends INBTSerializable<CompoundTag> {
  public static MachineTask NONE = new MachineTask() {

    @Override
    public CompoundTag serializeNBT(Provider provider) {
      return new CompoundTag();
    }

    @Override
    public void deserializeNBT(Provider provider, CompoundTag nbt) {
      // no-op
    }

    @Override
    public void tick() {
      // no-op
    }

    @Override
    public float getProgress() {
      return 1.0f;
    }

    @Override
    public boolean isCompleted() {
      return true;
    }

  };

  void tick();

  float getProgress();

  boolean isCompleted();
}
