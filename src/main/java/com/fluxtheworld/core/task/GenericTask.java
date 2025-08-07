package com.fluxtheworld.core.task;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;

public abstract class GenericTask implements SerializableTask {

  protected TaskState state;

  protected GenericTask() {
    this.state = TaskState.Initial;
  }

  @Override
  public final TaskState tick() {
    if (this.state instanceof TaskState.Initial) {
      this.state = invalidateState();
    }

    if (!(this.state instanceof TaskState.Active)) {
      return this.state;
    }

    this.state = this.doWork();

    return this.state;
  }

  @Override
  public final TaskState getState() {
    return this.state;
  }

  protected final void abort(@Nullable TaskIssue cause) {
    this.state = new TaskState.Aborted(cause);
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    CompoundTag tag = new CompoundTag();
    tag.putBoolean("Completed", this.state instanceof TaskState.Completed);
    return tag;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag tag) {
    this.state = tag.getBoolean("Completed") ? TaskState.Completed : TaskState.Initial;
  }

  /**
   * Invalidates the current task. This method should be called by the block entity when its dependencies
   * (e.g., inventory, internal state) change, as the task might no longer be valid after such changes.
   */
  @Override
  public final TaskState invalidate() {
    if (this.state instanceof TaskState.Active || this.state instanceof TaskState.Pending) {
      this.state = this.invalidateState();
    }

    return this.state;
  }

  protected abstract TaskState doWork();

  protected abstract TaskState invalidateState();
}
