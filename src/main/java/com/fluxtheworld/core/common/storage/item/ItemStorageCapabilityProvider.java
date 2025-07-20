package com.fluxtheworld.core.common.storage.item;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.core.common.storage.side_access.SideAccessConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemStorageCapabilityProvider implements ICapabilityProvider<BlockEntity, Direction, IItemHandler> {

  @Override
  public @Nullable IItemHandler getCapability(BlockEntity be, Direction side) {
    if (be instanceof ItemStorageProvider provider) {
      return provider.getItemStorage().getForSide(provider.getItemSideAccess(), side);
    }
    return null;
  }

  public interface ItemStorageProvider {
    ItemStorage getItemStorage();

    SideAccessConfig getItemSideAccess();
  }
}