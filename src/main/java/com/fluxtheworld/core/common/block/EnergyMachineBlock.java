package com.fluxtheworld.core.common.block;

import java.util.function.Supplier;

import com.fluxtheworld.core.common.block_entity.EnergyMachineBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class EnergyMachineBlock<BE extends EnergyMachineBlockEntity> extends MachineBlock<BE> {

  public EnergyMachineBlock(Supplier<BlockEntityType<? extends BE>> typeSupplier, Properties properties) {
    super(typeSupplier, properties);
  }

}
