package com.fluxtheworld.core.common.register.datagen;

import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DatagenHolder {

  public final LogicalSide side;
  public final DataProviderConstructor<? extends DataProvider> provider;

  public DatagenHolder(LogicalSide side, DataProviderConstructor<? extends DataProvider> provider) {
    this.side = side;
    this.provider = provider;
  }

  @FunctionalInterface
  public interface DataProviderConstructor<T extends DataProvider> {
    T create(PackOutput output, ExistingFileHelper existingFileHelper);
  }
}