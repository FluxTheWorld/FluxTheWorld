package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.recipe.MachineRecipe;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record AlloySmelterRecipe(SizedIngredient input0, SizedIngredient input1, ItemStack output, int processingTime, int energyUsage,
    float experienceReward)
    implements MachineRecipe {

  public boolean matches(ItemStorage storage) {
    final var input0 = storage.getStackInSlot("input0");
    final var input1 = storage.getStackInSlot("input1");
    return (this.input0.test(input0) && this.input1.test(input1)) || (this.input0.test(input1) && this.input1.test(input0));
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return AlloySmelterRegistry.RECIPE_SERIALIZER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return AlloySmelterRegistry.RECIPE_TYPE.get();
  }

  public static class Serializer implements RecipeSerializer<AlloySmelterRecipe> {
    public static final MapCodec<AlloySmelterRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder
        .group(
            SizedIngredient.FLAT_CODEC.fieldOf("input0").forGetter(AlloySmelterRecipe::input0),
            SizedIngredient.FLAT_CODEC.fieldOf("input1").forGetter(AlloySmelterRecipe::input1),
            ItemStack.CODEC.fieldOf("output").forGetter(AlloySmelterRecipe::output),
            Codec.INT.fieldOf("processing_time").forGetter(AlloySmelterRecipe::processingTime),
            Codec.INT.fieldOf("energy_usage").forGetter(AlloySmelterRecipe::energyUsage),
            Codec.FLOAT.fieldOf("experience_reward").forGetter(AlloySmelterRecipe::experienceReward))
        .apply(builder, AlloySmelterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlloySmelterRecipe> STREAM_CODEC = StreamCodec
        .composite(
            SizedIngredient.STREAM_CODEC, AlloySmelterRecipe::input0,
            SizedIngredient.STREAM_CODEC, AlloySmelterRecipe::input1,
            ItemStack.STREAM_CODEC, AlloySmelterRecipe::output,
            ByteBufCodecs.INT, AlloySmelterRecipe::processingTime,
            ByteBufCodecs.INT, AlloySmelterRecipe::energyUsage,
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
