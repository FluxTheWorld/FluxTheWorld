package com.fluxtheworld.core.menu;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import com.fluxtheworld.core.network.sync.DataSlot;
import com.fluxtheworld.core.network.sync.IntDataSlot;
import com.fluxtheworld.core.network.sync.payload.DataSlotPayload;
import com.fluxtheworld.core.network.sync.payload.IntDataSlotPayload;
import com.fluxtheworld.core.network.packet.DataSlotUpdatePayload;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericMenu extends AbstractContainerMenu {

  private final Inventory playerInventory;
  private final List<DataSlot<?>> dataSlots = new ArrayList<>();

  protected GenericMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory) {
    super(menuType, containerId);
    this.playerInventory = playerInventory;
  }

  // region Synchronization

  @SuppressWarnings("java:S2177")
  protected <T> DataSlot<T> addDataSlot(DataSlot<T> dataSlot) {
    dataSlots.add(dataSlot);
    return dataSlot;
  }

  @Override
  public void broadcastChanges() {
    super.broadcastChanges();
    for (int i = 0; i < dataSlots.size(); i++) {
      DataSlot<?> dataSlot = dataSlots.get(i);
      if (dataSlot.isDirty()) {
        // Assuming concrete DataSlot implementations have an encodePayload method
        // This will need to be handled polymorphically or through a common interface
        // For now, casting to IntDataSlot for demonstration
        if (dataSlot instanceof IntDataSlot intDataSlot) {
          DataSlotUpdatePayload payload = new DataSlotUpdatePayload(containerId, (short) i, intDataSlot.encodePayload());
          PacketDistributor.PLAYER.with(playerInventory.player).send(payload);
        }
      }
    }
  }

  // This method will be called on the client side to handle incoming updates
  public void handleDataSlotUpdate(short slotIndex, DataSlotPayload payload) {
    if (slotIndex >= 0 && slotIndex < dataSlots.size()) {
      DataSlot<?> dataSlot = dataSlots.get(slotIndex);
      // Assuming concrete DataSlot implementations have a decodePayload method
      // This will need to be handled polymorphically or through a common interface
      // For now, casting to IntDataSlot for demonstration
      if (dataSlot instanceof IntDataSlot intDataSlot && payload instanceof IntDataSlotPayload intPayload) {
        intDataSlot.decodePayload(intPayload);
      }
    }
  }

  // endregion

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

  @Override
  public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
    return ItemStack.EMPTY; // Basic implementation, needs proper logic for item transfer
  }
}
