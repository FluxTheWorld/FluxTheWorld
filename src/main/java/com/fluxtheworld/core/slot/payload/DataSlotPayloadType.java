package com.fluxtheworld.core.slot.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum DataSlotPayloadType {
  // @formatter:off
  INT(IntDataSlotPayload.STREAM_CODEC),
  FLOAT(FloatDataSlotPayload.STREAM_CODEC),
  ;
  // @formatter:on

  private final StreamCodec<FriendlyByteBuf, ? extends DataSlotPayload> streamCodec;

  DataSlotPayloadType(StreamCodec<FriendlyByteBuf, ? extends DataSlotPayload> streamCodec) {
    this.streamCodec = streamCodec;
  }

  @SuppressWarnings("unchecked")
  public StreamCodec<FriendlyByteBuf, DataSlotPayload> getStreamCodec() {
    return (StreamCodec<FriendlyByteBuf, DataSlotPayload>) streamCodec;
  }
}
