package com.fluxtheworld.common.block.alloy_smelter;

import com.fluxtheworld.common.registry.FTWBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlockEntity extends BlockEntity implements MenuProvider, Container {

  private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
  private int litTime;
  private int litDuration;
  private int cookingProgress;
  private int cookingTotalTime;

  protected final ContainerData dataAccess = new ContainerData() {
    @Override
    public int get(int index) {
      return switch (index) {
        case 0 -> AlloySmelterBlockEntity.this.litTime;
        case 1 -> AlloySmelterBlockEntity.this.litDuration;
        case 2 -> AlloySmelterBlockEntity.this.cookingProgress;
        case 3 -> AlloySmelterBlockEntity.this.cookingTotalTime;
        default -> 0;
      };
    }

    @Override
    public void set(int index, int value) {
      switch (index) {
        case 0 -> AlloySmelterBlockEntity.this.litTime = value;
        case 1 -> AlloySmelterBlockEntity.this.litDuration = value;
        case 2 -> AlloySmelterBlockEntity.this.cookingProgress = value;
        case 3 -> AlloySmelterBlockEntity.this.cookingTotalTime = value;
      }
    }

    @Override
    public int getCount() {
      return 4;
    }
  };

  private final RecipeType<?> recipeType; // Placeholder for now

  public AlloySmelterBlockEntity(BlockPos pos, BlockState state) {
    super(FTWBlockEntities.MY_BLOCK_ENTITY.get(), pos, state);
    this.recipeType = RecipeType.SMELTING; // Placeholder, will be replaced with custom recipe type
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, items, registries);
    this.litTime = tag.getInt("BurnTime");
    this.cookingProgress = tag.getInt("CookTime");
    this.cookingTotalTime = tag.getInt("CookTimeTotal");
    this.litDuration = this.getBurnDuration(this.items.get(1));
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    ContainerHelper.saveAllItems(tag, items, registries);
    tag.putInt("BurnTime", this.litTime);
    tag.putInt("CookTime", this.cookingProgress);
    tag.putInt("CookTimeTotal", this.cookingTotalTime);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("container.alloysmelter");
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new AlloySmelterMenu(id, inventory, this);
  }

  @Override
  public int getContainerSize() {
    return items.size();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemstack : items) {
      if (!itemstack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getItem(int index) {
    return items.get(index);
  }

  @Override
  public ItemStack removeItem(int index, int count) {
    return ContainerHelper.removeItem(items, index, count);
  }

  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return ContainerHelper.takeItem(items, index);
  }

  @Override
  public void setItem(int index, ItemStack stack) {
    items.set(index, stack);
    if (stack.getCount() > getMaxStackSize()) {
      stack.setCount(getMaxStackSize());
    }
  }

  @Override
  public boolean stillValid(Player player) {
    if (this.level.getBlockEntity(this.worldPosition) != this) {
      return false;
    } else {
      return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D,
          (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }
  }

  @Override
  public void clearContent() {
    items.clear();
  }
  private boolean isLit() {
    return this.litTime > 0;
  }

  public static void tick(Level level, BlockPos pos, BlockState state, AlloySmelterBlockEntity blockEntity) {
    if (level.isClientSide) {
      return;
    }

    boolean wasLit = blockEntity.isLit();
    if (wasLit) {
      blockEntity.litTime--;
    }

    ItemStack fuel = blockEntity.items.get(1);
    boolean hasFuel = !fuel.isEmpty();
    boolean hasInput = !blockEntity.items.get(0).isEmpty();

    if (blockEntity.isLit() || (hasFuel && hasInput)) {
      AlloySmelterRecipeInput recipeInput = new AlloySmelterRecipeInput(blockEntity.items.get(0), blockEntity.items.get(1));
      RecipeHolder<?> recipe = level.getRecipeManager().getRecipeFor(blockEntity.recipeType, recipeInput, level).orElse(null);

      if (recipe != null) {
        blockEntity.cookingTotalTime = 200; // TODO: Get from recipe
        if (!blockEntity.isLit() && canBurn(level.registryAccess(), recipe, blockEntity.items, 64, recipeInput)) {
          blockEntity.litTime = 100; // TODO: Get from fuel
          blockEntity.litDuration = blockEntity.litTime;
          if (blockEntity.isLit()) {
            if (fuel.hasCraftingRemainingItem()) {
              blockEntity.items.set(1, fuel.getCraftingRemainingItem());
            } else if (hasFuel) {
              fuel.shrink(1);
              if (fuel.isEmpty()) {
                blockEntity.items.set(1, fuel.getCraftingRemainingItem());
              }
            }
          }
        }

        if (blockEntity.isLit() && canBurn(level.registryAccess(), recipe, blockEntity.items, 64, recipeInput)) {
          blockEntity.cookingProgress++;
          if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
            blockEntity.cookingProgress = 0;
            burn(level.registryAccess(), recipe, blockEntity.items, 64, recipeInput);
          }
        } else {
          blockEntity.cookingProgress = 0;
        }
      }
    } else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
      blockEntity.cookingProgress = 0;
    }

    if (wasLit != blockEntity.isLit()) {
      state = state.setValue(com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlock.LIT, blockEntity.isLit());
      level.setBlock(pos, state, 3);
    }
  }

  private static boolean canBurn(net.minecraft.core.RegistryAccess registryAccess, RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AlloySmelterRecipeInput recipeInput) {
    if (inventory.get(0).isEmpty() || inventory.get(1).isEmpty() || recipe == null) {
      return false;
    } else {
      ItemStack resultStack = recipe.value().assemble(recipeInput, registryAccess);
      if (resultStack.isEmpty()) {
        return false;
      } else {
        ItemStack outputStack = inventory.get(2);
        if (outputStack.isEmpty()) {
          return true;
        } else if (!ItemStack.isSameItemSameComponents(outputStack, resultStack)) {
          return false;
        } else {
          return outputStack.getCount() + resultStack.getCount() <= maxStackSize && outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxStackSize();
        }
      }
    }
  }

  private static void burn(net.minecraft.core.RegistryAccess registryAccess, RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, AlloySmelterRecipeInput recipeInput) {
    if (recipe != null && canBurn(registryAccess, recipe, inventory, maxStackSize, recipeInput)) {
      ItemStack inputA = inventory.get(0);
      ItemStack inputB = inventory.get(1);
      ItemStack resultStack = recipe.value().assemble(recipeInput, registryAccess);
      ItemStack outputStack = inventory.get(2);

      if (outputStack.isEmpty()) {
        inventory.set(2, resultStack.copy());
      } else if (ItemStack.isSameItemSameComponents(outputStack, resultStack)) {
        outputStack.grow(resultStack.getCount());
      }

      inputA.shrink(1);
      inputB.shrink(1);
    }
  }

  private int getBurnDuration(ItemStack fuel) {
    if (fuel.isEmpty()) {
      return 0;
    } else {
      return fuel.getBurnTime(this.recipeType);
    }
  }
}