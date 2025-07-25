package com.fluxtheworld.core.menu;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.core.Preconditions;
import com.fluxtheworld.core.block_entity.MachineBlockEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.LogicalSide;

public abstract class MachineMenu<BE extends MachineBlockEntity, ML extends MachineMenuLayout<BE>>
    extends BlockEntityMenu<BE> implements MenuLayoutProvider<ML> {

  private @Nullable ML menuLayout;

  // client
  @SafeVarargs
  protected MachineMenu(
      @Nullable MenuType<?> menuType,
      int containerId,
      Inventory playerInventory,
      RegistryFriendlyByteBuf buf,
      BlockEntityType<? extends BE>... blockEntityTypes) {
    super(menuType, containerId, playerInventory, buf, blockEntityTypes);
    this.applyLayout(this.getClientMenuLayout());
  }

  // server
  protected MachineMenu(
      @Nullable MenuType<?> menuType,
      int containerId,
      Inventory playerInventory,
      BE blockEntity) {
    super(menuType, containerId, playerInventory, blockEntity);
    this.applyLayout(this.getServerMenuLayout());
  }

  // Copy-paste from:
  // https://docs.neoforged.net/docs/1.21.1/gui/menus#quickmovestack
  // Looks like is enought for first implementation
  //
  // Assume we have a data inventory of size 5
  // The inventory has 4 inputs (index 1 - 4) which outputs to a result slot
  // (index 0)
  // We also have the 27 player inventory slots and the 9 hotbar slots
  // As such, the actual slots are indexed like so:
  // - Data Inventory: Result (0), Inputs (1 - 4)
  // - Player Inventory (5 - 31)
  // - Player Hotbar (32 - 40)
  @Override
  public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
    // The quick moved slot stack
    ItemStack quickMovedStack = ItemStack.EMPTY;
    // The quick moved slot
    Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

    // If the slot is in the valid range and the slot is not empty
    if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
      // Get the raw stack to move
      ItemStack rawStack = quickMovedSlot.getItem();
      // Set the slot stack to a copy of the raw stack
      quickMovedStack = rawStack.copy();

      /*
       * The following quick move logic can be simplified to if in data inventory,
       * try to move to player inventory/hotbar and vice versa for containers
       * that cannot transform data (e.g. chests).
       */

      // If the quick move was performed on the data inventory result slot
      if (quickMovedSlotIndex == 0) {
        // Try to move the result slot into the player inventory/hotbar
        if (!this.moveItemStackTo(rawStack, 5, 41, true)) {
          // If cannot move, no longer quick move
          return ItemStack.EMPTY;
        }

        // Perform logic on result slot quick move
        quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
      }
      // Else if the quick move was performed on the player inventory or hotbar slot
      else if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {
        // Try to move the inventory/hotbar slot into the data inventory input slots
        if (!this.moveItemStackTo(rawStack, 1, 5, false)) {
          // If cannot move and in player inventory slot, try to move to hotbar
          if (quickMovedSlotIndex < 32) {
            if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
              // If cannot move, no longer quick move
              return ItemStack.EMPTY;
            }
          }
          // Else try to move hotbar into player inventory slot
          else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
            // If cannot move, no longer quick move
            return ItemStack.EMPTY;
          }
        }
      }
      // Else if the quick move was performed on the data inventory input slots, try
      // to move to player inventory/hotbar
      else if (!this.moveItemStackTo(rawStack, 5, 41, false)) {
        // If cannot move, no longer quick move
        return ItemStack.EMPTY;
      }

      if (rawStack.isEmpty()) {
        // If the raw stack has completely moved out of the slot, set the slot to the
        // empty stack
        quickMovedSlot.set(ItemStack.EMPTY);
      }
      else {
        // Otherwise, notify the slot that that the stack count has changed
        quickMovedSlot.setChanged();
      }

      /*
       * The following if statement and Slot#onTake call can be removed if the
       * menu does not represent a container that can transform stacks (e.g.
       * chests).
       */
      if (rawStack.getCount() == quickMovedStack.getCount()) {
        // If the raw stack was not able to be moved to another slot, no longer quick
        // move
        return ItemStack.EMPTY;
      }
      // Execute logic on what to do post move with the remaining stack
      quickMovedSlot.onTake(player, rawStack);
    }

    return quickMovedStack; // Return the slot stack
  }

  // region MenuLayout

  protected abstract ML createMenuLayout();

  @Override
  public ML getClientMenuLayout() {
    Preconditions.ensureSide(LogicalSide.CLIENT);

    if (this.menuLayout == null) {
      this.menuLayout = this.createMenuLayout();
      this.menuLayout.init();
      this.menuLayout.initClient();
    }

    return this.menuLayout;
  }

  @Override
  public ML getServerMenuLayout() {
    Preconditions.ensureSide(LogicalSide.SERVER);

    if (this.menuLayout == null) {
      this.menuLayout = this.createMenuLayout();
      this.menuLayout.init();
      this.menuLayout.initServer();
    }

    return this.menuLayout;
  }

  // endregion
}
