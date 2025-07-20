package com.fluxtheworld.core.network.sync.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface DataSlotPayload<T extends DataSlotPayload<T>> {
  StreamCodec<RegistryFriendlyByteBuf, T> type();

  DataSlotPayloadType getType();
}