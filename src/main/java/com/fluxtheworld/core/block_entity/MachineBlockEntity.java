package com.fluxtheworld.core.block_entity;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.item.ItemStorage;
import com.fluxtheworld.core.storage.item.ItemStorageCapabilityProvider.ItemStorageProvider;
import com.fluxtheworld.core.task.GenericTask;
import com.fluxtheworld.core.task.TaskHost;
import com.fluxtheworld.core.task.TaskHostProvider;
import com.fluxtheworld.core.util.CountdownTimer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends GenericBlockEntity implements MenuProvider {

  protected MachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
  }

  // region MenuProvider

  @Override
  public Component getDisplayName() {
    return getBlockState().getBlock().getName();
  }

  // endregion

  @Override
  public void serverTick() {
    super.serverTick();

    if (this instanceof TaskHostProvider provider) {
      provider.getTaskHost().tick();
    }

  }

  // region Tasks

  protected boolean haveNextTask() {
    return false;
  }

  protected GenericTask createNextTask() {
    return GenericTask.NONE;
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

    if (this instanceof ItemStorageProvider provider) {
      ItemStorage itemStorage = provider.getItemStorage();
      tag.put("ItemStorage", itemStorage.serializeNBT(registries));
    }

    if (this instanceof TaskHostProvider provider) {
      TaskHost taskHost = provider.getTaskHost();
      tag.put("TaskHost", taskHost.serializeNBT(registries));
    }

  }

  @Override
  protected void loadAdditional(CompoundTag tag, Provider registries) {
    super.loadAdditional(tag, registries);

    if (this instanceof ItemStorageProvider provider && tag.contains("ItemStorage")) {
      ItemStorage itemStorage = provider.getItemStorage();
      itemStorage.deserializeNBT(registries, tag.getCompound("ItemStorage"));
    }

    if (this instanceof TaskHostProvider provider && tag.contains("TaskHost")) {
      TaskHost taskHost = provider.getTaskHost();
      taskHost.deserializeNBT(registries, tag.getCompound("TaskHost"));
    }

  }

  //
}
