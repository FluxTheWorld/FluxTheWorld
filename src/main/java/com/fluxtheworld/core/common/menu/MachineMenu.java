package com.fluxtheworld.core.common.menu;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.core.common.block_entity.MachineBlockEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class MachineMenu<BE extends MachineBlockEntity> extends BlockEntityMenu<BE> {

  // client
  @SafeVarargs
  protected MachineMenu(
      @Nullable MenuType<?> menuType,
      int containerId,
      Inventory playerInventory,
      RegistryFriendlyByteBuf buf,
      BlockEntityType<? extends BE>... blockEntityTypes) {
    super(menuType, containerId, playerInventory, buf, blockEntityTypes);
  }

  // server
  protected MachineMenu(
      @Nullable MenuType<?> menuType,
      int containerId,
      Inventory playerInventory,
      BE blockEntity) {
    super(menuType, containerId, playerInventory, blockEntity);
  }

}
