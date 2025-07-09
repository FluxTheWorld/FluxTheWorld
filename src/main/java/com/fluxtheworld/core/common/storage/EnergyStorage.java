package com.fluxtheworld.core.common.storage;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface EnergyStorage extends IEnergyStorage, INBTSerializable<Tag> {
}
