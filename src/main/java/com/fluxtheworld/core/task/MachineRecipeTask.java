package com.fluxtheworld.core.task;

import javax.annotation.Nullable;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.recipe.MachineRecipe;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public abstract class MachineRecipeTask<I extends RecipeInput> implements GenericTask {

  protected Level level;
  protected RecipeHolder<MachineRecipe<I>> recipeHolder;
  protected float progress;

  protected MachineRecipeTask(Level level, @Nullable RecipeHolder<MachineRecipe<I>> recipeHolder) {
    this.level = level;
    this.recipeHolder = recipeHolder;
    this.progress = 0;
  }

  @Override
  public float getProgress() {
    if (this.recipeHolder == null) {
      return 0;
    }

    return Math.clamp(this.progress / this.getRecipe().processingTime(), 0, 1);
  }

  @Override
  public boolean isCompleted() {
    FTWMod.LOGGER.info("+100500 isCompleted {}", this.recipeHolder);
    if (this.recipeHolder == null) {
      return true;
    }

    return this.progress >= this.recipeHolder.value().processingTime();
  }

  protected MachineRecipe<I> getRecipe() {
    return this.recipeHolder.value();
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    CompoundTag nbt = new CompoundTag();
    nbt.putString("RecipeHolderId", this.recipeHolder.id().toString());
    nbt.putFloat("Progress", this.progress);
    return nbt;
  }

  @Override
  @SuppressWarnings({ "unchecked", "java:S3655" })
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    FTWMod.LOGGER.info("+100500 deserializeNBT {}", nbt);

    String id = nbt.getString("Id");
    ResourceLocation loc = ResourceLocation.parse(id);
    this.recipeHolder = (RecipeHolder<MachineRecipe<I>>) this.level.getRecipeManager().byKey(loc).get();

    this.progress = nbt.getFloat("Progress");
  }
}
