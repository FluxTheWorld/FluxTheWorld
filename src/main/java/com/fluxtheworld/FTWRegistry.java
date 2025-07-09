package com.fluxtheworld;

import java.util.Set;
import java.util.function.Function;

import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlock;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterBlockEntity;
import com.fluxtheworld.common.block.alloy_smelter.AlloySmelterMenu;
import com.fluxtheworld.core.common.registry.BlockEntityTypeHolder;
import com.fluxtheworld.core.common.registry.BlockHolder;
import com.fluxtheworld.core.common.registry.MenuTypeHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FTWRegistry {

  private FTWRegistry() {
  }

  private static final DeferredRegister.Blocks BLOCKS;
  private static final DeferredRegister.Items ITEMS;
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
  private static final DeferredRegister<MenuType<?>> MENU_TYPES;

  static {
    BLOCKS = DeferredRegister.createBlocks(FTWMod.MOD_ID);
    ITEMS = DeferredRegister.createItems(FTWMod.MOD_ID);
    BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FTWMod.MOD_ID);
    MENU_TYPES = DeferredRegister.create(Registries.MENU, FTWMod.MOD_ID);
  }

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    BLOCK_ENTITY_TYPES.register(modEventBus);
    MENU_TYPES.register(modEventBus);
  }

  // region Utils

  private static <T extends Block> DeferredBlock<T> registerBlock(
      Function<BlockHolder.Builder<T>, BlockHolder.Builder<T>> func) {
    BlockHolder<T> holder = func.apply(new BlockHolder.Builder<>()).build();
    return BLOCKS.register(holder.name(), holder.factory());
  }

  private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> registerBlockEntityType(
      Function<BlockEntityTypeHolder.Builder<T>, BlockEntityTypeHolder.Builder<T>> func) {
    BlockEntityTypeHolder<T> holder = func.apply(new BlockEntityTypeHolder.Builder<>()).build();
    return BLOCK_ENTITY_TYPES.register(holder.name(), holder.factory());
  }

  private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
      Function<MenuTypeHolder.Builder<T>, MenuTypeHolder.Builder<T>> func) {
    MenuTypeHolder<T> holder = func.apply(new MenuTypeHolder.Builder<>()).build();
    return MENU_TYPES.register(holder.name(), holder.factory());
  }

  // endregion

  // region AlloySmelter

  public static final DeferredBlock<AlloySmelterBlock> ALLOY_SMELTER_BLOCK = registerBlock(
      (builder) -> {
        return builder
            .name("alloy_smelter")
            .factory(() -> new AlloySmelterBlock(BlockBehaviour.Properties.of()));
      });

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlloySmelterBlockEntity>> ALLOY_SMELTER_BLOCK_ENTITY = registerBlockEntityType(
      (builder) -> {
        return builder
            .name("alloy_smelter")
            .factory(AlloySmelterBlockEntity::new)
            .blocks(ALLOY_SMELTER_BLOCK);
      });

  public static final DeferredHolder<MenuType<?>, MenuType<AlloySmelterMenu>> ALLOY_SMELTER_MENU = registerMenuType(
      (builder) -> {
        return builder
            .name("alloy_smelter")
            .factory(AlloySmelterMenu::new);
      });

  // TODO: Create Item holder like other Utils methods
  public static final DeferredHolder<net.minecraft.world.item.Item, net.minecraft.world.item.Item> MY_BETTER_BLOCK_ITEM = ITEMS
      .register(
          "alloy_smelter",
          () -> new net.minecraft.world.item.BlockItem(ALLOY_SMELTER_BLOCK.get(), new Properties()));

  // endregion
}
