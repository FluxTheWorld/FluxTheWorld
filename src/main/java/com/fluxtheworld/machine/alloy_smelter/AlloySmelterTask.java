package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;
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

    if (!recipe.matches(storage) || !storage.insertItem(SlotAccessTag.OUTPUT, recipe.output(), true).isEmpty()) {
      this.abort();
      return;
    }

    this.advance();
    // TODO: Consume energy

    if (this.isCompleted()) {
      storage.extractIngredient(SlotAccessTag.INPUT, recipe.input0(), false);
      storage.extractIngredient(SlotAccessTag.INPUT, recipe.input1(), false);
      storage.insertItem(SlotAccessTag.OUTPUT, recipe.output(), false);
    }
  }

}
