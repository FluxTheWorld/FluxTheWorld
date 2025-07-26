package com.fluxtheworld.machine.alloy_smelter;

import java.util.function.Supplier;

import com.fluxtheworld.core.task.MachineRecipeTask;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class AlloySmelterTask extends MachineRecipeTask<AlloySmelterRecipe.Input> {

  public AlloySmelterTask(ResourceLocation recipeId, Supplier<Level> levelSupplier) {
    super(recipeId, levelSupplier);
  }

  @Override
  public void tick() {
    this.progress += 1;
  }

}
