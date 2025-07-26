package com.fluxtheworld.core.task;

import javax.annotation.Nullable;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.recipe.MachineRecipe;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class MachineRecipeTask<BE extends MachineBlockEntity, R extends MachineRecipe> implements GenericTask {

  private final BE blockEntity;

  private ResourceLocation recipeId;
  private float progress;
  private boolean aborted;

  private @Nullable RecipeHolder<R> recipeHolder;

  protected MachineRecipeTask(BE blockEntity, ResourceLocation recipeId) {
    this.blockEntity = blockEntity;
    this.recipeId = recipeId;
    this.progress = 0;
    this.aborted = false;
  }

  @Override
  public float getProgress() {
    if (this.aborted) {
      return 0;
    }

    return Math.clamp(this.progress / this.getRecipe().processingTime(), 0, 1);
  }

  @Override
  public boolean isActive() {
    return !this.aborted && !this.isCompleted();
  }

  @Override
  public boolean isCompleted() {
    return this.progress >= this.getRecipe().processingTime();
  }

  protected void advance() {
    this.progress++;
  }

  protected void abort() {
    this.progress = 0;
    this.aborted = true;
  }

  protected BE getBlockEntity() {
    return this.blockEntity;
  }

  // TODO: Maybe pass Level instance as argument
  @SuppressWarnings({ "null", "unchecked", "java:S3655" })
  protected R getRecipe() {
    final var holder = this.recipeHolder;

    if (holder == null || !this.recipeId.equals(holder.id())) {
      final var loaded = this.blockEntity.getLevel().getRecipeManager().byKey(this.recipeId).get();
      this.recipeHolder = (RecipeHolder<R>) loaded;
    }

    return this.recipeHolder.value();
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    CompoundTag nbt = new CompoundTag();
    nbt.putString("RecipeId", this.recipeId.toString());
    nbt.putFloat("Progress", this.progress);
    return nbt;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    this.recipeId = ResourceLocation.parse(nbt.getString("RecipeId"));
    this.progress = nbt.getFloat("Progress");
  }
}
