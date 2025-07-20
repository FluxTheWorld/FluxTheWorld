package com.fluxtheworld.core.menu;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class BlockEntityMenu<BE extends BlockEntity> extends GenericMenu {

  private final BE blockEntity;

  // client
  @SafeVarargs
  protected BlockEntityMenu(
      @Nullable MenuType<?> menuType,
      int containerId,
      Inventory playerInventory,
      RegistryFriendlyByteBuf buf,
      BlockEntityType<? extends BE>... blockEntityTypes) {
    super(menuType, containerId, playerInventory);
    this.blockEntity = MenuUtils.getBlockEntityFrom(buf, playerInventory.player.level(), blockEntityTypes);
  }

  // server
  protected BlockEntityMenu(
      @Nullable MenuType<?> menuType,
      int containerId,
      Inventory playerInventory,
      BE blockEntity) {
    super(menuType, containerId, playerInventory);
    this.blockEntity = blockEntity;
  }

  public BE getBlockEntity() {
    return blockEntity;
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this.blockEntity, player);
  }
}
