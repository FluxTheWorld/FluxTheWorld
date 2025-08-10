package com.fluxtheworld.core.storage.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemStorageCapabilityProvider implements ICapabilityProvider<BlockEntity, Direction, IItemHandler> {

  @Override
  public @Nullable IItemHandler getCapability(BlockEntity be, @Nullable Direction side) {
    if (be instanceof ItemStorage.Provider provider) {
      return provider.getItemStorage().getForPipe(provider.getItemSideAccess(), side);
    }
    return null;
  }

}