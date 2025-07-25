package com.fluxtheworld.core.slot.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FloatDataSlotPayload(float value) implements DataSlotPayload {

  public static final StreamCodec<FriendlyByteBuf, FloatDataSlotPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT,
      FloatDataSlotPayload::value,
      FloatDataSlotPayload::new);

  @Override
  public DataSlotPayloadType type() {
    return DataSlotPayloadType.FLOAT;
  }

}