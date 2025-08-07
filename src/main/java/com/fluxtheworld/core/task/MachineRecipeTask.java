package com.fluxtheworld.core.task;

import javax.annotation.Nullable;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.recipe.MachineRecipe;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class MachineRecipeTask<BE extends MachineBlockEntity, R extends MachineRecipe> extends GenericTask {

  private final BE blockEntity;

  private ResourceLocation recipeId;
  private @Nullable RecipeHolder<R> recipeHolder;

  private float progress;

  protected MachineRecipeTask(BE blockEntity, ResourceLocation recipeId) {
    super();
    this.blockEntity = blockEntity;
    this.recipeId = recipeId;
    this.progress = 0;
  }

  @Override
  protected final TaskState doWork() {
    final TaskState state;

    if (this.getRecipe().processingTime() > this.progress) {
      state = this.doRecipe();
      if (state instanceof TaskState.Active) {
        this.progress += 1;
      }
    }
    else {
      state = this.attemptComplete();
    }

    return state;
  }

  @Override
  public final float getProgress() {
    return Math.clamp(this.progress / this.getRecipe().processingTime(), 0, 1);
  }

  protected final BE getBlockEntity() {
    return this.blockEntity;
  }

  // TODO: Maybe pass Level instance as argument
  @SuppressWarnings({ "null", "unchecked", "java:S3655" })
  protected final R getRecipe() {
    final var holder = this.recipeHolder;

    if (holder == null || !this.recipeId.equals(holder.id())) {
      final var loaded = this.blockEntity.getLevel().getRecipeManager().byKey(this.recipeId).get();
      this.recipeHolder = (RecipeHolder<R>) loaded;
    }

    return this.recipeHolder.value();
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    CompoundTag tag = super.serializeNBT(provider);
    tag.putString("RecipeId", this.recipeId.toString());
    tag.putFloat("Progress", this.progress);
    return tag;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag tag) {
    super.deserializeNBT(provider, tag);
    this.recipeId = ResourceLocation.parse(tag.getString("RecipeId"));
    this.progress = tag.getFloat("Progress");
  }

  /**
   * Attempts to complete the task. This method performs side-effects such as consuming input items
   * and producing output results.
   *
   * @return A {@link TaskState.Completed} state if all side-effects are performed successfully,
   *         otherwise a {@link TaskState.Pending} or {@link TaskState.Aborted} state.
   *         If the task returns to a pending state, it can be re-attempted on the next tick after
   *         {@link #invalidate()} is called.
   */
  protected abstract TaskState attemptComplete();

  protected abstract TaskState invalidateState();

  protected abstract TaskState doRecipe();
}
