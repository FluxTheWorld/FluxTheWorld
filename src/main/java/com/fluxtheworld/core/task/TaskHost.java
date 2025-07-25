package com.fluxtheworld.core.task;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface TaskHost extends INBTSerializable<CompoundTag> {
  public void tick();
}