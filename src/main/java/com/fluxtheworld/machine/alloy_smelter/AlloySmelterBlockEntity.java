package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider;
import com.fluxtheworld.core.storage.item.MachineItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider.ItemStorageProvider;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.task.TaskHost;
import com.fluxtheworld.core.task.TaskHostProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends MachineBlockEntity implements ItemStorageProvider, TaskHostProvider {

  public static final ItemStorageCapabilityProvider ITEM_STORAGE_PROVIDER = new ItemStorageCapabilityProvider();

  private ItemStorage itemStorage;
  private ItemSlotAccessConfig itemSlotAccessConfig;
  private SideAccessConfig sideAccess;
  private MachineTaskHost taskHost;

  public AlloySmelterBlockEntity(BlockPos worldPosition, BlockState blockState) {
    super(AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get(), worldPosition, blockState);
    this.itemSlotAccessConfig = ItemSlotAccessConfig.builder().inputSlot("input1").inputSlot("input2").outputSlot("output").build();
    this.itemStorage = new MachineItemStorage(this, this.itemSlotAccessConfig);
    this.sideAccess = new SideAccessConfig();
  }

  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new AlloySmelterMenu(containerId, playerInventory, this);
  }

  @Override
  public ItemStorage getItemStorage() {
    return this.itemStorage;
  }

  @Override
  public SideAccessConfig getItemSideAccess() {
    return this.sideAccess;
  }

  @Override
  public TaskHost getTaskHost() {
    return this.taskHost;
  }

}