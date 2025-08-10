package com.fluxtheworld.core.storage.fluid;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidStorageCapabilityProvider implements ICapabilityProvider<BlockEntity, Direction, IFluidHandler> {

  @Override
  public @Nullable IFluidHandler getCapability(BlockEntity be, @Nullable Direction side) {
    if (be instanceof FluidStorage.Provider provider) {
      return provider.getFluidStorage().getForPipe(provider.getFluidSideAccess(), side).getHandler();
    }
    return null;
  }

}