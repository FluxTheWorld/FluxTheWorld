package com.fluxtheworld.core.network.sync.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IntDataSlotPayload(int value) implements DataSlotPayload<IntDataSlotPayload> {
  public static final StreamCodec<RegistryFriendlyByteBuf, IntDataSlotPayload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT,
      IntDataSlotPayload::value,
      IntDataSlotPayload::new);

  @Override
  public StreamCodec<RegistryFriendlyByteBuf, IntDataSlotPayload> type() {
    return STREAM_CODEC;
  }

  @Override
  public DataSlotPayloadType getType() {
    return DataSlotPayloadType.INT;
  }
}