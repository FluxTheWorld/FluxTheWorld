package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.block_entity.BlockEntityChange;
import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider;
import com.fluxtheworld.core.storage.item.ItemStorageChangeListener;
import com.fluxtheworld.core.storage.item.ItemStorageProvider;
import com.fluxtheworld.core.storage.energy.EnergyStorage;
import com.fluxtheworld.core.storage.energy.EnergyStorageCapabilityProvider.EnergyStorageProvider;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.task.SerializableTask;
import com.fluxtheworld.core.task.TaskProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends MachineBlockEntity
    implements ItemStorageProvider, ItemStorageChangeListener, EnergyStorageProvider, TaskProvider {

  public static final ItemStorageCapabilityProvider ITEM_STORAGE_PROVIDER = new ItemStorageCapabilityProvider();

  private final ItemStorage itemStorage;
  private final ItemSlotAccessConfig itemSlotAccess;
  private final SideAccessConfig sideAccess;
  private final EnergyStorage energyStorage;
  private SerializableTask task;

  public AlloySmelterBlockEntity(BlockPos worldPosition, BlockState blockState) {
    super(AlloySmelterRegistry.BLOCK_ENTITY_TYPE.get(), worldPosition, blockState);
    this.itemSlotAccess = ItemSlotAccessConfig.builder().inputSlot("input0").inputSlot("input1").outputSlot("output").build();
    this.itemStorage = new ItemStorage(this.itemSlotAccess, this);
    this.sideAccess = new SideAccessConfig();
    this.energyStorage = new EnergyStorage(128_000);
    this.task = SerializableTask.NONE;
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
  public void onItemStorageChanged(int slot) {
    this.setChanged(BlockEntityChange.ItemStorage);
  }

  @Override
  public SideAccessConfig getItemSideAccess() {
    return this.sideAccess;
  }

  @Override
  public EnergyStorage getEnergyStorage() {
    return this.energyStorage;
  }

  @Override
  public SerializableTask getCurrentTask() {
    return this.task;
  }

  @Override
  @SuppressWarnings("null")
  public SerializableTask createNextTask() {
    this.task = SerializableTask.NONE;

    final var recipes = this.level.getRecipeManager().getAllRecipesFor(AlloySmelterRegistry.RECIPE_TYPE.get());
    for (RecipeHolder<AlloySmelterRecipe> holder : recipes) {
      if (holder.value().matches(this.getItemStorage())) {
        this.task = new AlloySmelterTask(this, holder.id());
      }
    }

    return this.task;
  }

  @Override
  @SuppressWarnings({ "null", "java:S4449" })
  public SerializableTask createEmptyTask() {
    this.task = new AlloySmelterTask(this, null);
    return this.task;
  }

}