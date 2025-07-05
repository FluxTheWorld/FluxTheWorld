package com.fluxtheworld.electricfurnace;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nullable;

public class ElectricFurnaceContainer extends AbstractContainerMenu {
    private final ElectricFurnaceBlockEntity blockEntity;
    private final Level level;

    // This will be registered later in FluxTheWorld.java
    public static MenuType<?> TYPE;

    // Server-side constructor
    public ElectricFurnaceContainer(int id, Inventory playerInventory, ElectricFurnaceBlockEntity blockEntity) {
        super(TYPE, id); // Use TYPE here, it will be set during registration
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();

        // Furnace inventory slots (input, fuel, output)
        // TODO: Add actual item handler for block entity
        this.addSlot(new Slot(blockEntity, 0, 56, 17)); // Input slot
        this.addSlot(new Slot(blockEntity, 1, 56, 53)); // Fuel slot
        this.addSlot(new Slot(blockEntity, 2, 116, 35)); // Output slot

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    // Client-side constructor (used by MenuScreens.register)
    public ElectricFurnaceContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, getBlockEntity(playerInventory));
    }

    private static ElectricFurnaceBlockEntity getBlockEntity(Inventory playerInventory) {
        // This is a placeholder. In a real scenario, you'd pass the BlockPos from the client.
        // For now, we'll assume it's always at a specific position for testing or rely on server sync.
        // This will be properly handled when the BlockEntity is registered and linked.
        return new ElectricFurnaceBlockEntity(BlockPos.ZERO, null); // Placeholder
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 2) { // Output slot
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == 1 || index == 0) { // Fuel or Input slot
                if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) { // Player inventory to furnace
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        // Ensure the container is still valid (e.g., player is near the block)
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }
}