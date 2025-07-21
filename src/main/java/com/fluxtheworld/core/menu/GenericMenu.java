package com.fluxtheworld.core.menu;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.slot.DataSlot;
import com.fluxtheworld.core.slot.LocalDataSlotProxy;
import com.fluxtheworld.core.slot.MutableDataSlot;
import com.fluxtheworld.core.slot.SyncDataSlotsPacket;
import com.fluxtheworld.core.slot.SyncDataSlotsPacket.ListEntry;
import com.fluxtheworld.core.slot.payload.DataSlotPayload;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class GenericMenu extends AbstractContainerMenu {

  private final Inventory playerInventory;
  private final List<DataSlot<?>> dataSlots;

  protected GenericMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory) {
    super(menuType, containerId);
    this.playerInventory = playerInventory;
    this.dataSlots = new ArrayList<>();
  }

  // region Synchronization

  @SuppressWarnings("java:S2177")
  protected <T> void addDataSlot(MutableDataSlot<T> dataSlot) {
    if (playerInventory.player instanceof LocalPlayer) {
      this.dataSlots.add(new LocalDataSlotProxy<>(dataSlot, this.dataSlots.size(), this.containerId));
    }
    else {
      this.dataSlots.add(dataSlot);
    }
  }

  public void handleSyncDataSlotsPacket(SyncDataSlotsPacket packet) {
    for (ListEntry entry : packet.entries()) {
      int index = entry.index();

      if (index < 0 || index > this.dataSlots.size()) {
        FTWMod.LOGGER.warn("Attempted to access non-existent data slot at index {}.", index);
        continue;
      }

      DataSlotPayload payload = entry.payload();
      DataSlot<?> slot = this.dataSlots.get(index);
      slot.decodePayload(payload);
    }
  }

  @Override
  public void broadcastChanges() {
    super.broadcastChanges();

    if (playerInventory.player instanceof ServerPlayer player) {
      List<SyncDataSlotsPacket.ListEntry> entries = new ArrayList<>();

      for (int index = 0; index < this.dataSlots.size(); index++) {
        DataSlot<?> dataSlot = this.dataSlots.get(index);
        if (dataSlot.checkAndClearUpdateFlag()) {
          entries.add(new ListEntry(index, dataSlot.encodePayload()));
        }
      }

      if (!entries.isEmpty()) {
        PacketDistributor.sendToPlayer(player, new SyncDataSlotsPacket(containerId, entries));
      }
    }
  }

  @Override
  public void sendAllDataToRemote() {
    super.sendAllDataToRemote();

    if (playerInventory.player instanceof ServerPlayer player) {
      List<SyncDataSlotsPacket.ListEntry> entries = new ArrayList<>();

      for (int index = 0; index < this.dataSlots.size(); index++) {
        DataSlot<?> dataSlot = this.dataSlots.get(index);
        dataSlot.checkAndClearUpdateFlag();
        entries.add(new ListEntry(index, dataSlot.encodePayload()));
      }

      if (!entries.isEmpty()) {
        PacketDistributor.sendToPlayer(player, new SyncDataSlotsPacket(containerId, entries));
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
