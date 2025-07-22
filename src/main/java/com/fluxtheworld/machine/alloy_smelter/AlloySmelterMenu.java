package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.menu.MachineMenu;
import com.fluxtheworld.core.storage.item.ItemStorage;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AlloySmelterMenu extends MachineMenu<AlloySmelterBlockEntity> {

  // client
  public AlloySmelterMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
    super(AlloySmelterRegistry.MENU_TYPE.get(), containerId, playerInventory, buf, AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get());

    this.addSlots();
  }

  // server
  public AlloySmelterMenu(int containerId, Inventory inventory, AlloySmelterBlockEntity blockEntity) {
    super(AlloySmelterRegistry.MENU_TYPE.get(), containerId, inventory, blockEntity);

    this.addSlots();
  }

  private void addSlots() {
    ItemStorage storage = this.getBlockEntity().getItemStorage();

    this.addSlot(new SlotItemHandler(storage, 0, 20, 36));
    this.addSlot(new SlotItemHandler(storage, 1, 40, 36));
    this.addSlot(new SlotItemHandler(storage, 2, 60, 36));

    this.addPlayerInventory(8, 84);
  }
}