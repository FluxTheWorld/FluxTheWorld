package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider;
import com.fluxtheworld.core.storage.item.ItemStorageLayout;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider.ItemStorageProvider;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends MachineBlockEntity implements ItemStorageProvider {

  public static final ItemStorageCapabilityProvider ITEM_STORAGE_PROVIDER = new ItemStorageCapabilityProvider();

  private ItemStorage itemStorage;
  private SideAccessConfig itemAccessConfig;

  public AlloySmelterBlockEntity(BlockPos worldPosition, BlockState blockState) {
    super(AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get(), worldPosition, blockState);
    // TODO: Maybe it better to create MachineItemStorage and provide MachineBlockEntity to args
    this.itemStorage = new ItemStorage(
        ItemStorageLayout.builder()
            .slotCount(3)
            .setCanExtract(i -> i == 3)
            .setCanInsert(i -> i == 1 || i == 2)
            .build(),
        (slot) -> this.setChanged());
    this.itemAccessConfig = new SideAccessConfig();
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
    return this.itemAccessConfig;
  }

}