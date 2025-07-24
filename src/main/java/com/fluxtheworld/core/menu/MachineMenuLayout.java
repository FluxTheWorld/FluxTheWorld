package com.fluxtheworld.core.menu;

import java.util.ArrayList;
import java.util.List;

import com.fluxtheworld.core.Preconditions;
import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.gui.component.GenericWidget;
import com.fluxtheworld.core.gui.component.ItemSlotWidget;
import com.fluxtheworld.core.slot.MutableDataSlot;
import com.fluxtheworld.core.storage.item.ItemStorage;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class MachineMenuLayout<T extends MachineBlockEntity> {

  protected final Inventory playerInventory;
  protected final T blockEntity;

  protected int width;
  protected int height;

  private final List<Slot> slots;
  private final List<MutableDataSlot<?>> dataSlots;
  private final List<GenericWidget> widgets;
  private final List<GenericWidget> renderables;

  private final boolean isClientSide;

  protected MachineMenuLayout(Inventory playerInventory, T blockEntity) {
    this.playerInventory = playerInventory;
    this.blockEntity = blockEntity;

    this.width = 176;
    this.height = 166;

    this.slots = new ArrayList<>();
    this.dataSlots = new ArrayList<>();
    this.widgets = new ArrayList<>();
    this.renderables = new ArrayList<>();

    Player player = playerInventory.player;
    this.isClientSide = !(player instanceof ServerPlayer);

    if (isClientSide) {
      this.init();
      this.initServer();
    }
    else {
      this.init();
      this.initClient();
    }
  }

  protected abstract void init();

  protected void initClient() {
  }

  protected void initServer() {
  }

  protected <T extends Slot> T addSlot(T slot) {
    this.slots.add(slot);
    return slot;
  }

  protected SlotItemHandler addItemSlot(ItemStorage storage, String name, int x, int y) {
    SlotItemHandler slot = new SlotItemHandler(storage.getForMenu(), storage.getSlotIndex(name), x, y);
    this.addSlot(slot);
    if (isClientSide) {
      this.addItemSlotWidget(x, y);
    }
    return slot;
  }

  public <T> MutableDataSlot<T> addDataSlot(MutableDataSlot<T> dataSlot) {
    this.dataSlots.add(dataSlot);
    return dataSlot;
  }

  public void addPlayerInventory(int x, int y) {
    this.addPlayerMainInventory(x, y);
    this.addPlayerHotbarInventory(x, y + 58);
  }

  public void addPlayerMainInventory(int xStart, int yStart) {
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        addPlayerInventorySlot(x + y * 9 + 9, xStart + x * 18, yStart + y * 18);
      }
    }
  }

  public void addPlayerHotbarInventory(int x, int y) {
    for (int i = 0; i < 9; i++) {
      addPlayerInventorySlot(i, x + i * 18, y);
    }
  }

  private Slot addPlayerInventorySlot(int index, int x, int y) {
    Slot slot = new Slot(this.playerInventory, index, x, y);
    this.addSlot(slot);
    if (isClientSide) {
      this.addItemSlotWidget(x, y);
    }
    return slot;
  }

  protected ItemSlotWidget addItemSlotWidget(int x, int y) {
    Preconditions.ensureSide(LogicalSide.CLIENT);
    return this.addItemSlotWidget(x - 1, y - 1, 18, 18);
  }

  protected ItemSlotWidget addItemSlotWidget(int x, int y, int width, int height) {
    Preconditions.ensureSide(LogicalSide.CLIENT);
    ItemSlotWidget widget = new ItemSlotWidget(x, y, width, height);
    return this.addRenderable(widget);
  }

  protected <T extends GenericWidget> T addRenderable(T renderable) {
    Preconditions.ensureSide(LogicalSide.CLIENT);
    this.renderables.add(renderable);
    return renderable;
  }

  // region Getters

  public List<Slot> getSlots() {
    return slots;
  }

  public List<MutableDataSlot<?>> getDataSlots() {
    return dataSlots;
  }

  public List<GenericWidget> getWidgets() {
    return widgets;
  }

  public List<GenericWidget> getRenderables() {
    return renderables;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  // endregion
}
