package com.fluxtheworld.core.common.block;

import java.util.function.Supplier;

import com.fluxtheworld.core.common.block_entity.SteamMachineBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SteamMachineBlock<BE extends SteamMachineBlockEntity> extends MachineBlock<BE> {

  public SteamMachineBlock(Supplier<BlockEntityType<? extends BE>> typeSupplier, Properties properties) {
    super(typeSupplier, properties);
  }

}
