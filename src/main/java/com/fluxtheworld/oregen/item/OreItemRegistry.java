package com.fluxtheworld.oregen.item;

import com.fluxtheworld.core.register.CommonRegister;
import com.fluxtheworld.core.register.item.DeferredItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;

public class OreItemRegistry {

    private OreItemRegistry() {
    }

    public static DeferredItem<Item> RAW_IRON;
    public static DeferredItem<Item> IRON_DUST;
    public static DeferredItem<Item> IRON_INGOT;

    public static void register(CommonRegister register, Dist dist) {
        RAW_IRON = register.items.register("raw_iron", () ->
                new Item(new Item.Properties()));

        IRON_DUST = register.items.register("iron_dust", () ->
                new Item(new Item.Properties()));

        IRON_INGOT = register.items.register("iron_ingot", () ->
                new Item(new Item.Properties()));
    }
}