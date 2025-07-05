package com.fluxtheworld.electricfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;

public class ElectricFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    // This will be registered later in FluxTheWorld.java
    public static BlockEntityType<?> TYPE;

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.electric_furnace");
    }

    @Override
    public ElectricFurnaceContainer createMenu(int id, net.minecraft.world.entity.player.Inventory playerInventory) {
        return new ElectricFurnaceContainer(id, playerInventory, this);
    }
}