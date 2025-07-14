package com.fluxtheworld.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatagenRegistry {

  private DatagenRegistry() {
  }

  private static final List<Consumer<GatherDataEvent>> providers;

  static {
    providers = new ArrayList<>();
  }

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(GatherDataEvent.class, (event) -> {
      for (Consumer<GatherDataEvent> provider : DatagenRegistry.providers) {
        provider.accept(event);
      }
    });
  }

  public static void register(Consumer<GatherDataEvent> provider) {
    providers.add(provider);
  }

  // --- --- ---
}