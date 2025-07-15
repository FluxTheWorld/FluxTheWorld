package com.fluxtheworld.core.common.register;

import com.fluxtheworld.core.common.register.block.BlockRegister;
import com.fluxtheworld.core.common.register.block_entity_type.BlockEntityTypeRegister;
import com.fluxtheworld.core.common.register.datagen.DatagenRegister;
import com.fluxtheworld.core.common.register.item.ItemRegister;
import com.fluxtheworld.core.common.register.menu_type.MenuTypeRegister;

import net.neoforged.bus.api.IEventBus;

public class CommonRegister {

  public final BlockRegister blocks;
  public final BlockEntityTypeRegister blockEntityTypes;
  public final ItemRegister items;
  public final MenuTypeRegister menuTypes;
  public final DatagenRegister datagen;

  public CommonRegister(String namespace) {
    this.blocks = new BlockRegister(namespace);
    this.blockEntityTypes = new BlockEntityTypeRegister(namespace);
    this.items = new ItemRegister(namespace);
    this.menuTypes = new MenuTypeRegister(namespace);
    this.datagen = new DatagenRegister(namespace);
  }

  public void register(IEventBus eventBus) {
    this.blocks.register(eventBus);
    this.blockEntityTypes.register(eventBus);
    this.items.register(eventBus);
    this.menuTypes.register(eventBus);
    this.datagen.register(eventBus);
  }

  public CommonRegister asCommon() {
    return this;
  }

}
