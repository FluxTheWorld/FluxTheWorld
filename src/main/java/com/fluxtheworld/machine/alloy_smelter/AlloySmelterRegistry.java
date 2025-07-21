package com.fluxtheworld.machine.alloy_smelter;

import java.util.Set;

import com.fluxtheworld.core.block.MachineBlock;
import com.fluxtheworld.core.register.ClientRegister;
import com.fluxtheworld.core.register.CommonRegister;
import com.fluxtheworld.core.register.block.DeferredBlock;
import com.fluxtheworld.core.register.block_entity_type.DeferredBlockEntityType;
import com.fluxtheworld.core.register.container_screen.ContainerScreenHolder;
import com.fluxtheworld.core.register.item.DeferredItem;
import com.fluxtheworld.core.register.menu_type.DeferredMenuType;

import net.minecraft.core.Direction;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
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
      p.models().getBuilder("alloy_smelter_faces")
          .customLoader(CompositeModelBuilder::begin)
          .child("face_north", p.models().nested()
              .parent(p.models().getExistingFile(p.modLoc("block/face_north")))
              .renderType(p.mcLoc("cutout"))
              .texture("face", p.modLoc("block/alloy_smelter_front")))
          .end();

      p.models().getBuilder("alloy_smelter_faces_on")
          .customLoader(CompositeModelBuilder::begin)
          .child("face_north", p.models().nested()
              .parent(p.models().getExistingFile(p.modLoc("block/face_north")))
              .renderType(p.mcLoc("cutout"))
              .texture("face", p.modLoc("block/alloy_smelter_front_on")))
          .end();

      p.models().getBuilder("alloy_smelter")
          .parent(p.models().getExistingFile(p.mcLoc("block/cube")))
          .texture("particle", p.modLoc("block/machine_side"))
          .customLoader(CompositeModelBuilder::begin)
          .child("machine", p.models().nested().parent(p.models().getExistingFile(p.modLoc("block/machine"))))
          .child("alloy_smelter_faces", p.models().nested().parent(p.models().getExistingFile(p.modLoc("block/alloy_smelter_faces"))))
          .end();

      p.models().getBuilder("alloy_smelter_on")
          .parent(p.models().getExistingFile(p.mcLoc("block/cube")))
          .texture("particle", p.modLoc("block/machine_side"))
          .customLoader(CompositeModelBuilder::begin)
          .child("machine", p.models().nested().parent(p.models().getExistingFile(p.modLoc("block/machine"))))
          .child("alloy_smelter_faces", p.models().nested().parent(p.models().getExistingFile(p.modLoc("block/alloy_smelter_faces_on"))))
          .end();

      p.itemModels().getBuilder("alloy_smelter")
          .parent(p.itemModels().getExistingFile(p.mcLoc("block/cube")))
          .customLoader(CompositeModelBuilder::begin)
          .child("machine", p.itemModels().nested().parent(p.models().getExistingFile(p.modLoc("block/machine"))))
          .child("alloy_smelter_faces", p.itemModels().nested().parent(p.models().getExistingFile(p.modLoc("block/alloy_smelter_faces"))))
          .end();

      p.getVariantBuilder(BLOCK.get()).forAllStates(state -> {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();

        String model = state.getValue(MachineBlock.LIT) ? "alloy_smelter_on" : "alloy_smelter";
        builder.modelFile(p.models().getExistingFile(p.modLoc(model)));

        Direction facing = state.getValue(MachineBlock.FACING);
        if (facing == Direction.EAST)
          builder.rotationY(90);
        if (facing == Direction.SOUTH)
          builder.rotationY(180);
        if (facing == Direction.WEST)
          builder.rotationY(270);

        return builder.build();
      });
    });

    if (dist.isClient()) {
      ClientRegister clientRegister = (ClientRegister) register;
      clientRegister.containerScreen.register(() -> {
        return new ContainerScreenHolder<>(MENU_TYPE.get(), AlloySmelterScreen::new);
      });
    }
  }

}
