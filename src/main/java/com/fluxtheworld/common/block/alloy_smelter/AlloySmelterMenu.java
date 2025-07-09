package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.core.common.menu.MachineMenu;
import com.fluxtheworld.core.common.registry.MenuTypeHolder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterMenu extends MachineMenu<AlloySmelterBlockEntity> {

  public static final MenuTypeHolder<AlloySmelterMenu> TYPE = new MenuTypeHolder.Builder<AlloySmelterMenu>()
      .build();

  // client
  public AlloySmelterMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    super(AlloySmelterMenu.TYPE.get(), containerId, playerInventory, buf, AlloySmelterBlockEntity.TYPE.get());
  }

  // server
  public AlloySmelterMenu(int pContainerId, Inventory inventory, AlloySmelterBlockEntity blockEntity) {
    super(AlloySmelterMenu.TYPE.get(), pContainerId, inventory, blockEntity);
  }
}