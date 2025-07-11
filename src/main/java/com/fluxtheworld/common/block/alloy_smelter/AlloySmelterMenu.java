package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.core.common.menu.MachineMenu;
import com.fluxtheworld.registry.BlockEntityTypeRegistry;
import com.fluxtheworld.registry.MenuTypeRegistry;
import com.fluxtheworld.registry.core.MenuTypeHolder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterMenu extends MachineMenu<AlloySmelterBlockEntity> {

  // client
  public AlloySmelterMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    super(MenuTypeRegistry.ALLOY_SMELTER.get(), containerId, playerInventory, buf,
        BlockEntityTypeRegistry.ALLOY_SMELTER.get());
  }

  // server
  public AlloySmelterMenu(int containerId, Inventory inventory, AlloySmelterBlockEntity blockEntity) {
    super(MenuTypeRegistry.ALLOY_SMELTER.get(), containerId, inventory, blockEntity);
  }
}