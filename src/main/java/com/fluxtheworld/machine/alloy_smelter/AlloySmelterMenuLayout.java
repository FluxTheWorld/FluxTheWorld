package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.menu.MachineMenuLayout;

import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;

public class AlloySmelterMenuLayout extends MachineMenuLayout<AlloySmelterBlockEntity> {

  public AlloySmelterMenuLayout(Inventory playerInventory, AlloySmelterBlockEntity blockEntity) {
    super(playerInventory, blockEntity);
  }

  @Override
  protected void init() {
    IItemHandler storage = this.blockEntity.getItemStorage().getForMenu();

    this.addItemSlot(storage, 0, 20, 36);
    this.addItemSlot(storage, 1, 40, 36);
    this.addItemSlot(storage, 2, 60, 36);

    this.addPlayerInventory(8, 84);
  }

}
