package com.fluxtheworld.core.common.block_entity;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.core.common.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.common.storage.side_access.SideAccessMode;
import com.fluxtheworld.core.common.task.MachineTask;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity extends GenericBlockEntity implements MenuProvider {

  protected MachineTask task;
  protected SideAccessConfig itemAccess;
  protected SideAccessConfig fluidAccess;

  protected MachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    this(type, worldPosition, blockState, new SideAccessConfig(SideAccessMode.DISABLED), new SideAccessConfig(SideAccessMode.DISABLED));
    this.task = MachineTask.NONE;
  }

  protected MachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState,
      SideAccessConfig itemAccess, SideAccessConfig fluidAccess) {
    super(type, worldPosition, blockState);
    this.task = MachineTask.NONE;
    this.itemAccess = itemAccess;
    this.fluidAccess = fluidAccess;
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
