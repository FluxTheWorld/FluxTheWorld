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
  private @Nullable RecipeHolder<R> recipeHolder;

  private float progress;
  private TaskState state;

  protected MachineRecipeTask(BE blockEntity, ResourceLocation recipeId) {
    this.blockEntity = blockEntity;
    this.recipeId = recipeId;
    this.progress = 0;
    this.state = new TaskState.Pending(null);
  }

  @Override
  public final TaskState tick() {
    if (!(this.state instanceof TaskState.Active)) {
      return this.state;
    }

    if (this.getRecipe().processingTime() > this.progress) {
      this.state = this.doWork();
      if (state instanceof TaskState.Active) {
        this.progress += 1;
      }
    }
    else {
      this.state = this.attemptComplete();
    }

    return this.state;
  }

  @Override
  public float getProgress() {
    return Math.clamp(this.progress / this.getRecipe().processingTime(), 0, 1);
  }

  @Override
  public TaskState getState() {
    return this.state;
  }

  protected void abort(@Nullable TaskIssue cause) {
    this.state = new TaskState.Aborted(cause);
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
    CompoundTag tag = new CompoundTag();
    tag.putString("RecipeId", this.recipeId.toString());
    tag.putFloat("Progress", this.progress);
    tag.putBoolean("Completed", this.state instanceof TaskState.Completed);
    return tag;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag tag) {
    this.recipeId = ResourceLocation.parse(tag.getString("RecipeId"));
    this.progress = tag.getFloat("Progress");
    this.state = tag.getBoolean("Completed") ? TaskState.Completed : new TaskState.Pending(null);
  }

  /**
   * Invalidates the current task. This method should be called by the block entity when its dependencies
   * (e.g., inventory, internal state) change, as the task might no longer be valid after such changes.
   */
  public final void invalidate() {
    this.state = this.invalidateState();
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

  /**
   * Performs one tick of work for the machine recipe task.
   *
   * @return {@link TaskState.Active} if work was performed successfully.
   */
  protected abstract TaskState doWork();

  protected abstract TaskState invalidateState();
}
