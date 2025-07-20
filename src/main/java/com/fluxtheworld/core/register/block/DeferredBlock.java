package com.fluxtheworld.core.register.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> implements ItemLike {

  protected DeferredBlock(ResourceKey<Block> key) {
    super(key);
  }

  @Override
  public Item asItem() {
    return this.get().asItem();
  }

  // region Utils
  public ItemStack toStack() {
    return this.toStack(1);
  }

  public ItemStack toStack(int count) {
    ItemStack stack = this.asItem().getDefaultInstance();
    if (stack.isEmpty()) {
      throw new IllegalStateException("Block does not have a corresponding item: " + this.key);
    }
    stack.setCount(count);
    return stack;
  }
  // endregion

}
