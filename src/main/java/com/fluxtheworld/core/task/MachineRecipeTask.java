package com.fluxtheworld.core.task;

import com.fluxtheworld.core.recipe.MachineRecipe;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;

public class MachineRecipeTask implements GenericTask {

  public MachineRecipeTask(MachineRecipe recipe) {
  }

  @Override
  public void tick() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'tick'");
  }

  @Override
  public float getProgress() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getProgress'");
  }

  @Override
  public boolean isCompleted() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isCompleted'");
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'serializeNBT'");
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deserializeNBT'");
  }
}
