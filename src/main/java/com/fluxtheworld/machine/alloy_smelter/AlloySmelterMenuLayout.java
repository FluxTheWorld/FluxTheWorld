package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.menu.MachineMenuLayout;
import com.fluxtheworld.core.storage.item.ItemStorage;

import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterMenuLayout extends MachineMenuLayout<AlloySmelterBlockEntity> {

  public AlloySmelterMenuLayout(Inventory playerInventory, AlloySmelterBlockEntity blockEntity) {
    super(playerInventory, blockEntity);
  }

  @Override
  protected void init() {
    ItemStorage storage = this.blockEntity.getItemStorage();

    this.addItemSlot(storage, "input1", 20, 36);
    this.addItemSlot(storage, "input2", 40, 36);
    this.addItemSlot(storage, "output", 60, 36);

    this.addPlayerInventory(8, 84);
  }

}
