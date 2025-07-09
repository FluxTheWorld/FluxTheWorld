package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.FTWRegistry;
import com.fluxtheworld.core.common.menu.MachineMenu;
import com.fluxtheworld.core.common.registry.MenuTypeHolder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterMenu extends MachineMenu<AlloySmelterBlockEntity> {

  // client
  public AlloySmelterMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    super(FTWRegistry.ALLOY_SMELTER_MENU.get(), containerId, playerInventory, buf,
        FTWRegistry.ALLOY_SMELTER_BLOCK_ENTITY.get());
  }

  // server
  public AlloySmelterMenu(int containerId, Inventory inventory, AlloySmelterBlockEntity blockEntity) {
    super(FTWRegistry.ALLOY_SMELTER_MENU.get(), containerId, inventory, blockEntity);
  }
}