package com.fluxtheworld.core.register.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DatagenRegister {

  private final String namespace;
  private final List<DatagenHolder> holders = new ArrayList<>();

  public DatagenRegister(String namespace) {
    this.namespace = namespace;
  }

  public void register(IEventBus eventBus) {
    eventBus.addListener(GatherDataEvent.class, event -> {
      DataGenerator generator = event.getGenerator();
      PackOutput output = generator.getPackOutput();
      ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
      CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

      for (DatagenHolder holder : holders) {
        boolean run = (event.includeClient() && holder.side.isClient()) || (event.includeServer() && holder.side.isServer());
        generator.addProvider(run, holder.provider.create(output, existingFileHelper));
      }
    });
  }

  public void register(LogicalSide side, DatagenHolder.DataProviderConstructor<? extends DataProvider> provider) {
    this.holders.add(new DatagenHolder(side, provider));
  }

  public void registerBlockStateProvider(Consumer<BlockStateProvider> generate) {
    this.holders.add(new DatagenHolder(LogicalSide.CLIENT, (PackOutput output, ExistingFileHelper existingFileHelper) -> {
      return new BlockStateProvider(output, this.namespace, existingFileHelper) {
        @Override
        protected void registerStatesAndModels() {
          generate.accept(this);
        }
      };
    }));
  }
}