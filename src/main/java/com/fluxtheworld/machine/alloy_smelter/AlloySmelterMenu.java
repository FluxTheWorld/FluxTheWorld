package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.menu.MachineMenu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterMenu extends MachineMenu<AlloySmelterBlockEntity> {
  public final AlloySmelterMenuLayout layout;

  // client
  public AlloySmelterMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    super(AlloySmelterRegistry.MENU_TYPE.get(), containerId, playerInventory, buf, AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get());

    this.layout = new AlloySmelterMenuLayout(playerInventory, this.getBlockEntity());
    this.applyLayout(layout);
  }

  // server
  public AlloySmelterMenu(int containerId, Inventory inventory, AlloySmelterBlockEntity blockEntity) {
    super(AlloySmelterRegistry.MENU_TYPE.get(), containerId, inventory, blockEntity);

    this.layout = new AlloySmelterMenuLayout(inventory, this.getBlockEntity());
    this.applyLayout(layout);
  }
}