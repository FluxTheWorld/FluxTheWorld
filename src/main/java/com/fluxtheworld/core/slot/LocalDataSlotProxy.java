package com.fluxtheworld.core.slot;

import java.util.List;

import com.fluxtheworld.core.slot.payload.DataSlotPayload;

import net.neoforged.neoforge.network.PacketDistributor;

public class LocalDataSlotProxy<T> implements MutableDataSlot<T> {
  private final MutableDataSlot<T> original;
  private final int index;
  private final int containerId;

  public LocalDataSlotProxy(MutableDataSlot<T> original, int index, int containerId) {
    this.original = original;
    this.index = index;
    this.containerId = containerId;
  }

  @Override
  public T get() {
    return this.original.get();
  }

  @Override
  public void set(T value) {
    this.original.set(value);

    if (this.checkAndClearUpdateFlag()) {
      SyncDataSlotsPacket.ListEntry entry = new SyncDataSlotsPacket.ListEntry(this.index, this.encodePayload());
      PacketDistributor.sendToServer(new SyncDataSlotsPacket(this.containerId, List.of(entry)));
    }
  }

  @Override
  public boolean checkAndClearUpdateFlag() {
    return this.original.checkAndClearUpdateFlag();
  }

  @Override
  public DataSlotPayload encodePayload() {
    return this.original.encodePayload();
  }

  @Override
  public void decodePayload(DataSlotPayload payload) {
    this.original.decodePayload(payload);
  }

}
