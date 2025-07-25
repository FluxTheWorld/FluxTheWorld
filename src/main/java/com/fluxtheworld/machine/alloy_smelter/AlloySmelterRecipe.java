package com.fluxtheworld.machine.alloy_smelter;

import java.util.List;

import com.fluxtheworld.core.recipe.MachineRecipe;
import com.fluxtheworld.core.recipe.OutputStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record AlloySmelterRecipe(List<SizedIngredient> input, ItemStack output, int energyCost, float experienceReward)
    implements MachineRecipe<AlloySmelterRecipe.Input> {

  @Override
  public boolean matches(Input input, Level level) {
    throw new UnsupportedOperationException("Unimplemented method 'matches'");
  }

  @Override
  public List<OutputStack> craft(Input input, RegistryAccess registry) {
    return null;
  }

  @Override
  public List<OutputStack> getResultStacks(RegistryAccess registry) {
    return List.of(OutputStack.of(this.output));
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return AlloySmelterRegistry.RECIPE_SERIALIZER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return AlloySmelterRegistry.RECIPE_TYPE.get();
  }

  public record Input(List<ItemStack> inputs) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
      if (slot >= inputs.size()) {
        throw new IllegalArgumentException("No item for index " + slot);
      }

      return inputs.get(slot);
    }

    @Override
    public int size() {
      return inputs.size();
    }

  }

  public static class Serializer implements RecipeSerializer<AlloySmelterRecipe> {
    public static final MapCodec<AlloySmelterRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder
        .group(
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("input").forGetter(AlloySmelterRecipe::input),
            ItemStack.CODEC.fieldOf("output").forGetter(AlloySmelterRecipe::output),
            Codec.INT.fieldOf("energy_cost").forGetter(AlloySmelterRecipe::energyCost),
            Codec.FLOAT.fieldOf("experience_reward").forGetter(AlloySmelterRecipe::experienceReward))
        .apply(builder, AlloySmelterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlloySmelterRecipe> STREAM_CODEC = StreamCodec
        .composite(
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), AlloySmelterRecipe::input,
            ItemStack.STREAM_CODEC, AlloySmelterRecipe::output,
            ByteBufCodecs.INT, AlloySmelterRecipe::energyCost,
            ByteBufCodecs.FLOAT, AlloySmelterRecipe::experienceReward,
            AlloySmelterRecipe::new);

    @Override
    public MapCodec<AlloySmelterRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AlloySmelterRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

}
