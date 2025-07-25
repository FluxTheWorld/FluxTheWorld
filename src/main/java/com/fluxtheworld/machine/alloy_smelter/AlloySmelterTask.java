package com.fluxtheworld.machine.alloy_smelter;

import javax.annotation.Nullable;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.recipe.MachineRecipe;
import com.fluxtheworld.core.task.MachineRecipeTask;
import com.fluxtheworld.machine.alloy_smelter.AlloySmelterRecipe.Input;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public class AlloySmelterTask extends MachineRecipeTask<AlloySmelterRecipe.Input> {

  protected AlloySmelterTask(Level level, @Nullable RecipeHolder<MachineRecipe<Input>> recipeHolder) {
    super(level, recipeHolder);
  }

  @Override
  public void tick() {
    FTWMod.LOGGER.info("+100500 Tick {}", this.progress);
    this.progress += 1;
  }

}
