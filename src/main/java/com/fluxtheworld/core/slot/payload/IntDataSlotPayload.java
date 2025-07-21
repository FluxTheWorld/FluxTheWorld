package com.fluxtheworld.core.slot.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IntDataSlotPayload(int value) implements DataSlotPayload {

  public static final StreamCodec<FriendlyByteBuf, IntDataSlotPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT,
      IntDataSlotPayload::value,
      IntDataSlotPayload::new);

  @Override
  public DataSlotPayloadType type() {
    return DataSlotPayloadType.INT;
  }

}