package com.fluxtheworld.core.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MenuUtils {

  private MenuUtils() {
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T extends BlockEntity> T getBlockEntityFrom(
      RegistryFriendlyByteBuf buf,
      Level level,
      BlockEntityType<? extends T>... types) {
    BlockPos pos = buf.readBlockPos();

    if (!level.isLoaded(pos)) {
      throw new IllegalStateException(
          "Unable to open menu for block at " + pos + " as it is not loaded on the client!");
    }

    BlockEntity blockEntity = level.getBlockEntity(pos);

    if (blockEntity == null) {
      throw new IllegalStateException("Unable to find block entity at " + pos + " on the client!");
    }

    for (var type : types) {
      if (blockEntity.getType() == type) {
        return (T) blockEntity;
      }
    }

    throw new IllegalStateException("Block entity at " + pos + " is the wrong type on the client!");
  }
}
