package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.task.MachineRecipeTask;

import net.minecraft.resources.ResourceLocation;

public class AlloySmelterTask extends MachineRecipeTask<AlloySmelterBlockEntity, AlloySmelterRecipe> {

  public AlloySmelterTask(AlloySmelterBlockEntity blockEntity, ResourceLocation recipeId) {
    super(blockEntity, recipeId);
  }

  @Override
  public void tick() {
    final var blockEntity = this.getBlockEntity();
    final var storage = blockEntity.getItemStorage();
    final var recipe = this.getRecipe();

    if (!recipe.matches(storage) || !storage.insertItem("output", recipe.output(), true).isEmpty()) {
      this.abort();
      return;
    }

    this.advance();
    // TODO: Consume energy

    if (this.isCompleted()) {
      storage.insertItem("output", recipe.output().copy(), false);
    }
  }

}
