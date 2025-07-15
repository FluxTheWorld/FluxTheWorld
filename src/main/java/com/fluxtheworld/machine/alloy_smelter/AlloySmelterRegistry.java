package com.fluxtheworld.machine.alloy_smelter;

import java.util.Set;

import com.fluxtheworld.core.common.register.ClientRegister;
import com.fluxtheworld.core.common.register.CommonRegister;
import com.fluxtheworld.core.common.register.block.DeferredBlock;
import com.fluxtheworld.core.common.register.block_entity_type.DeferredBlockEntityType;
import com.fluxtheworld.core.common.register.container_screen.ContainerScreenHolder;
import com.fluxtheworld.core.common.register.item.DeferredItem;
import com.fluxtheworld.core.common.register.menu_type.DeferredMenuType;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.loaders.CompositeModelBuilder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class AlloySmelterRegistry {

  private AlloySmelterRegistry() {
  }

  public static DeferredBlock<AlloySmelterBlock> BLOCK;
  public static DeferredItem<BlockItem> ITEM;
  public static DeferredBlockEntityType<BlockEntityType<AlloySmelterBlockEntity>> BLOCK_ENTITY_TYPE;
  public static DeferredMenuType<MenuType<AlloySmelterMenu>> MENU_TYPE;

  public static void register(CommonRegister register, Dist dist) {
    BLOCK = register.blocks.register("alloy_smelter", () -> {
      return new AlloySmelterBlock(BlockBehaviour.Properties.of());
    });

    ITEM = register.items.register("alloy_smelter", () -> {
      return new BlockItem(BLOCK.get(), new Item.Properties());
    });

    BLOCK_ENTITY_TYPE = register.blockEntityTypes.register("alloy_smelter", () -> {
      return new BlockEntityType<>(AlloySmelterBlockEntity::new, Set.of(BLOCK.get()), null);
    });

    MENU_TYPE = register.menuTypes.register("alloy_smelter", () -> {
      return IMenuTypeExtension.create(AlloySmelterMenu::new);
    });

    register.datagen.registerBlockStateProvider((p) -> {
      BlockModelBuilder machineModel = p.models().cubeBottomTop("machine",
          p.modLoc("block/machine_side"), p.modLoc("block/machine_bottom"), p.modLoc("block/machine_top"));

      BlockModelBuilder alloySmelterModel = p.models()
          .withExistingParent("alloy_smelter", p.mcLoc("minecraft:block/cube"))
          .texture("north", p.modLoc("block/alloy_smelter_front"));

      BlockModelBuilder alloySmelterModelCombined = p.models().getBuilder("alloy_smelter_combined")
          .customLoader(CompositeModelBuilder::begin)
          .child("machine", machineModel)
          .child("alloy_smelter", alloySmelterModel)
          .end();

      p.simpleBlockWithItem(BLOCK.get(), alloySmelterModelCombined);
    });

    if (dist.isClient()) {
      ClientRegister clientRegister = (ClientRegister) register;
      clientRegister.containerScreen.register(() -> {
        return new ContainerScreenHolder<>(MENU_TYPE.get(), AlloySmelterScreen::new);
      });
    }
  }

}
