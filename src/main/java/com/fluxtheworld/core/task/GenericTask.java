package com.fluxtheworld.core.task;

import com.fluxtheworld.FTWMod;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface GenericTask extends INBTSerializable<CompoundTag> {
  public static GenericTask NONE = new GenericTask() {

    @Override
    public void tick() {
      // no-op
      FTWMod.LOGGER.info("GenericTask {} tick", EffectiveSide.get().name());
    }

    @Override
    public float getProgress() {
      return 0.0f;
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

  boolean isCompleted();
}
