package com.fluxtheworld.core.menu;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;

import net.minecraft.world.entity.player.Inventory;

public abstract class MachineMenuLayout<T extends MachineBlockEntity> extends MenuLayout {
  protected final T blockEntity;

  protected MachineMenuLayout(Inventory playerInventory, T blockEntity) {
    super(playerInventory);
    this.blockEntity = blockEntity;
  }
}
