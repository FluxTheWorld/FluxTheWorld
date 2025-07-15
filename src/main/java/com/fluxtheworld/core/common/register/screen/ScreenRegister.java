package com.fluxtheworld.core.common.register.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

// ScreenRegister will not extend DeferredRegister as it's not a registry-based system.
// It will manage screen factories and register them via RegisterMenuScreensEvent.
public class ScreenRegister {

    private final String namespace;
    // Map to store MenuType to ScreenFactory mappings
    private final Map<MenuType<?>, ScreenFactory<?, ?>> screenFactories = new HashMap<>();

    public ScreenRegister(String namespace) {
        this.namespace = namespace;
    }

    // Register method for a MenuType and its corresponding ScreenFactory
    public <M extends AbstractContainerMenu, S extends Screen> DeferredScreen<M, S> register(MenuType<M> menuType, ScreenFactory<M, S> screenFactory) {
        screenFactories.put(menuType, screenFactory);
        return new DeferredScreen<>(menuType, screenFactory);
    }

    // ScreenFactory functional interface
    @FunctionalInterface
    public interface ScreenFactory<M extends AbstractContainerMenu, S extends Screen> {
        S create(M menu, Inventory inventory, Component title);
    }

    // Event handler for RegisterMenuScreensEvent
    @SubscribeEvent
    public void onRegisterScreens(RegisterMenuScreensEvent event) {
        screenFactories.forEach((menuType, screenFactory) -> {
            // This cast is safe because we put the correct types into the map
            event.register((MenuType<AbstractContainerMenu>) menuType, (net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor<AbstractContainerMenu>) screenFactory);
        });
    }
}