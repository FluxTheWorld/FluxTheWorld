package com.fluxtheworld.core.common.block_entity;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends GenericBlockEntity implements MenuProvider {

  public MachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
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

  //
}
