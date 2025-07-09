package com.fluxtheworld.core.common.menu;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public abstract class GenericMenu extends AbstractContainerMenu {

  private final Inventory playerInventory;

  protected GenericMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory) {
    super(menuType, containerId);
    this.playerInventory = playerInventory;
  }

  // region Inventory Utilities

  protected Inventory getPlayerInventory() {
    return playerInventory;
  }

  protected void addPlayerInventory(int x, int y) {
    addPlayerMainInventory(x, y);
    addPlayerHotbarInventory(x, y + 58);
  }
 
  protected void addPlayerMainInventory(int xStart, int yStart) {
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        addSlot(createPlayerSlot(x + y * 9 + 9, xStart + x * 18, yStart + y * 18));
      }
    }
  }

  protected void addPlayerHotbarInventory(int x, int y) {
    for (int i = 0; i < 9; i++) {
      addSlot(createPlayerSlot(i, x + i * 18, y));
    }
  }

  protected Slot createPlayerSlot(int slot, int x, int y) {
    return new Slot(this.playerInventory, slot, x, y);
  }

  // endregion

}
