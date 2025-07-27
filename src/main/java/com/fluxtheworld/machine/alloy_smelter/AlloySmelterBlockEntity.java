package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider.ItemStorageProvider;
import com.fluxtheworld.core.storage.item.MachineItemStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.task.GenericTask;
import com.fluxtheworld.core.task.TaskProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends MachineBlockEntity implements ItemStorageProvider, TaskProvider {

  public static final ItemStorageCapabilityProvider ITEM_STORAGE_PROVIDER = new ItemStorageCapabilityProvider();

  private final ItemStorage itemStorage;
  private final ItemSlotAccessConfig itemSlotAccess;
  private final SideAccessConfig sideAccess;
  private GenericTask task;

  public AlloySmelterBlockEntity(BlockPos worldPosition, BlockState blockState) {
    super(AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get(), worldPosition, blockState);
    this.itemSlotAccess = ItemSlotAccessConfig.builder().inputSlot("input0").inputSlot("input1").outputSlot("output").build();
    this.itemStorage = new MachineItemStorage(this, this.itemSlotAccess);
    this.sideAccess = new SideAccessConfig();
    this.task = GenericTask.NONE;
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
  public GenericTask getCurrentTask() {
    return this.task;
  }

  @Override
  public GenericTask createNextTask() {
    this.task = GenericTask.NONE;

    final var recipes = this.getLevel().getRecipeManager().getAllRecipesFor(AlloySmelterRegistry.RECIPE_TYPE.get());
    for (RecipeHolder<AlloySmelterRecipe> holder : recipes) {
      if (holder.value().matches(this.getItemStorage())) {
        this.task = new AlloySmelterTask(this, holder.id());
      }
    }
    
    return this.task;
  }

  @Override
  @SuppressWarnings({ "null", "java:S4449" })
  public GenericTask createEmptyTask() {
    this.task = new AlloySmelterTask(this, null);
    return this.task;
  }

}