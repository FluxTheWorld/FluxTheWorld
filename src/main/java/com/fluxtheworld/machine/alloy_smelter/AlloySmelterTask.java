package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;
import com.fluxtheworld.core.task.MachineRecipeTask;
import com.fluxtheworld.core.task.TaskState;

import net.minecraft.resources.ResourceLocation;

public class AlloySmelterTask extends MachineRecipeTask<AlloySmelterBlockEntity, AlloySmelterRecipe> {

  public AlloySmelterTask(AlloySmelterBlockEntity blockEntity, ResourceLocation recipeId) {
    super(blockEntity, recipeId);
  }

  @Override
  public TaskState doRecipe() {
    final var energy = this.getBlockEntity().getEnergyStorage();
    final var recipe = this.getRecipe();

    final var extracted = energy.extractEnergy(recipe.energyUsage(), false);
    if (extracted != recipe.energyUsage()) {
      return new TaskState.Pending(null);
    }

    return TaskState.Active;
  }

  @Override
  public TaskState invalidateState() {
    return TaskState.Active;
  }

  @Override
  public TaskState attemptComplete() {
    if (!this.craft(true)) {
      return new TaskState.Pending(null);
    }

    this.craft(false);
    return TaskState.Completed;
  }

  // TODO: Maybe split into two parts, consume and add results
  // In that case we can provide a better Pending issue and set Aborted if extract is not possible
  private boolean craft(boolean simulate) {
    final var recipe = this.getRecipe();
    final var storage = this.getBlockEntity().getItemStorage();

    final var input0 = storage.extractIngredient(SlotAccessTag.INPUT, recipe.input0(), simulate);
    final var input1 = storage.extractIngredient(SlotAccessTag.INPUT, recipe.input1(), simulate);
    final var output = storage.insertItem(SlotAccessTag.OUTPUT, recipe.output(), simulate);

    return !input0.isEmpty() && !input1.isEmpty() && output.isEmpty();
  }
}
