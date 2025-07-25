package com.fluxtheworld.machine.alloy_smelter;

import javax.annotation.Nullable;

import com.fluxtheworld.FTW;
import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.recipe.MachineRecipe;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider.ItemStorageProvider;
import com.fluxtheworld.core.storage.item.MachineItemStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.task.GenericTask;
import com.fluxtheworld.core.task.TaskProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
    this.itemSlotAccess = ItemSlotAccessConfig.builder().inputSlot("input1").inputSlot("input2").outputSlot("output").build();
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
    ResourceLocation loc = FTW.loc("alloy_smelter/example");
    this.task = new AlloySmelterTask(this.level,
        (RecipeHolder<MachineRecipe<AlloySmelterRecipe.Input>>) this.level.getRecipeManager().byKey(loc).get());
    return this.task;
  }

  @Override
  public GenericTask createEmptyTask() {
    this.task = new AlloySmelterTask(this.level, null);
    return this.task;
  }

}