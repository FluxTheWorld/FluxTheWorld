package com.fluxtheworld.internal.block.ore;

import com.fluxtheworld.core.register.CommonRegister;
import com.fluxtheworld.core.register.block.DeferredBlock;
import com.fluxtheworld.core.register.item.DeferredItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;

public class OreBlockRegistry {
    private OreBlockRegistry() {}

    public static DeferredBlock<Block> IRON_ORE;
    public static DeferredBlock<Block> DENSE_IRON_ORE;
    public static DeferredBlock<Block> DEPLETED_IRON_ORE;

    public static DeferredItem<BlockItem> IRON_ORE_ITEM;
    public static DeferredItem<BlockItem> DENSE_IRON_ORE_ITEM;
    public static DeferredItem<BlockItem> DEPLETED_IRON_ORE_ITEM;

    public static void register(CommonRegister register, Dist dist) {

        // Base ore block properties
        BlockBehaviour.Properties oreProperties = BlockBehaviour.Properties.of()
                .strength(3.0F, 3.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);

        // IRON_ORE (same as vanilla)
        IRON_ORE = register.blocks.register("iron_ore", () ->
                new Block(oreProperties)
        );
        IRON_ORE_ITEM = register.items.register("iron_ore", () ->
                new BlockItem(IRON_ORE.get(), new Item.Properties())
        );

        // DENSE_IRON_ORE (harder)
        DENSE_IRON_ORE = register.blocks.register("dense_iron_ore", () ->
                new Block(oreProperties.strength(6.0F, 6.0F))
        );
        DENSE_IRON_ORE_ITEM = register.items.register("dense_iron_ore", () ->
                new BlockItem(DENSE_IRON_ORE.get(), new Item.Properties())
        );

        // DEPLETED_IRON_ORE (weaker)
        DEPLETED_IRON_ORE = register.blocks.register("depleted_iron_ore", () ->
                new Block(oreProperties.strength(1.0F, 1.0F))
        );
        DEPLETED_IRON_ORE_ITEM = register.items.register("depleted_iron_ore", () ->
                new BlockItem(DEPLETED_IRON_ORE.get(), new Item.Properties())
        );
    }
}
