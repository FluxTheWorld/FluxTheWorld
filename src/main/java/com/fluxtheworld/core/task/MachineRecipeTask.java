package com.fluxtheworld.core.task;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.fluxtheworld.core.recipe.MachineRecipe;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public abstract class MachineRecipeTask<I extends RecipeInput> implements GenericTask {

  protected ResourceLocation recipeId;
  protected float progress;

  protected Supplier<Level> levelSupplier;
  private @Nullable RecipeHolder<MachineRecipe<I>> recipeHolder;

  // TODO: Maybe move level instance into tick method
  protected MachineRecipeTask(ResourceLocation recipeId, Supplier<Level> levelSupplier) {
    this.recipeId = recipeId;
    this.progress = 0;
    this.levelSupplier = levelSupplier;
  }

  @Override
  public float getProgress() {
    return Math.clamp(this.progress / this.getRecipe().processingTime(), 0, 1);
  }

  @Override
  public boolean isCompleted() {
    return this.progress >= this.getRecipe().processingTime();
  }

  // TODO: Maybe pass Level instance as argument
  @SuppressWarnings({ "null", "unchecked", "java:S3655" })
  protected MachineRecipe<I> getRecipe() {
    final var holder = this.recipeHolder;

    if (holder == null || !this.recipeId.equals(holder.id())) {
      final var loaded = this.levelSupplier.get().getRecipeManager().byKey(this.recipeId).get();
      this.recipeHolder = (RecipeHolder<MachineRecipe<I>>) loaded;
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
