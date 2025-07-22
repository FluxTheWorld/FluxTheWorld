package com.fluxtheworld.core.block_entity;

import javax.annotation.Nullable;

import com.fluxtheworld.FTW;
import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider.ItemStorageProvider;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.task.MachineTask;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends GenericBlockEntity implements MenuProvider {

  protected MachineTask task;

  protected MachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
    this.task = MachineTask.NONE;
  }

  // region MenuProvider

  @Override
  public Component getDisplayName() {
    return getBlockState().getBlock().getName();
  }

  // endregion

  // region Serialization

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    // Turn on syncing on block update
    // https://docs.neoforged.net/docs/1.21.1/blockentities/#syncing-on-block-update
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, Provider registries) {
    super.saveAdditional(tag, registries);
    if (this instanceof ItemStorageProvider itemStorageProvider) {
      ItemStorage itemStorage = itemStorageProvider.getItemStorage();
      tag.put("ItemStorage", itemStorage.serializeNBT(registries));
    }
    FTWMod.LOGGER.info("saveAdditional \n" + tag.toString());
  }

  @Override
  protected void loadAdditional(CompoundTag tag, Provider registries) {
    super.loadAdditional(tag, registries);
    if (this instanceof ItemStorageProvider itemStorageProvider) {
      ItemStorage itemStorage = itemStorageProvider.getItemStorage();
      if (tag.contains("ItemStorage")) {
        itemStorage.deserializeNBT(registries, tag.getCompound("ItemStorage"));
      }
    }
    FTWMod.LOGGER.info("loadAdditional \n" + tag.toString());
  }

  //
}
