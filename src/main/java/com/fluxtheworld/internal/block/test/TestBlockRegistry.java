package com.fluxtheworld.internal.block.test;

import com.fluxtheworld.core.register.CommonRegister;
import com.fluxtheworld.core.register.block.DeferredBlock;
import com.fluxtheworld.core.register.item.DeferredItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;

public class TestBlockRegistry {

  private TestBlockRegistry() {
  }

  public static DeferredBlock<TestBlock> BLOCK;
  public static DeferredItem<BlockItem> ITEM;

  public static void register(CommonRegister register, Dist dist) {
    BLOCK = register.blocks.register("test_block", () -> {
      return new TestBlock();
    });

    ITEM = register.items.register("test_block", () -> {
      return new BlockItem(BLOCK.get(), new Item.Properties());
    });
  }

}
