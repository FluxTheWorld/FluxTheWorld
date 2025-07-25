package com.fluxtheworld.core.register;

import com.fluxtheworld.core.register.block.BlockRegister;
import com.fluxtheworld.core.register.block_entity_type.BlockEntityTypeRegister;
import com.fluxtheworld.core.register.datagen.DatagenRegister;
import com.fluxtheworld.core.register.item.ItemRegister;
import com.fluxtheworld.core.register.menu_type.MenuTypeRegister;
import com.fluxtheworld.core.register.recipe_serializer.RecipeSerializerRegister;
import com.fluxtheworld.core.register.recipe_type.RecipeTypeRegister;

import net.neoforged.bus.api.IEventBus;

public abstract class CommonRegister {

  public final BlockRegister blocks;
  public final BlockEntityTypeRegister blockEntityTypes;
  public final ItemRegister items;
  public final MenuTypeRegister menuTypes;
  public final RecipeTypeRegister recipeTypes;
  public final RecipeSerializerRegister recipeSerializers;
  public final DatagenRegister datagen;

  protected CommonRegister(String namespace) {
    this.blocks = new BlockRegister(namespace);
    this.blockEntityTypes = new BlockEntityTypeRegister(namespace);
    this.items = new ItemRegister(namespace);
    this.menuTypes = new MenuTypeRegister(namespace);
    this.recipeTypes = new RecipeTypeRegister(namespace);
    this.recipeSerializers = new RecipeSerializerRegister(namespace);
    this.datagen = new DatagenRegister(namespace);
  }

  public void register(IEventBus eventBus) {
    this.blocks.register(eventBus);
    this.blockEntityTypes.register(eventBus);
    this.items.register(eventBus);
    this.menuTypes.register(eventBus);
    this.recipeTypes.register(eventBus);
    this.recipeSerializers.register(eventBus);
    this.datagen.register(eventBus);
  }

  public CommonRegister asCommon() {
    return this;
  }

}
