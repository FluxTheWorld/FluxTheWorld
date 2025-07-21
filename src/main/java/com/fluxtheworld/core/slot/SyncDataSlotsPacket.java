package com.fluxtheworld.core.slot;

import java.util.List;

import com.fluxtheworld.core.slot.payload.DataSlotPayload;
import com.fluxtheworld.core.slot.payload.DataSlotPayloadType;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SyncDataSlotsPacket(int containerId, List<ListEntry> payloads) {

  public record ListEntry(int index, DataSlotPayload payload) {
    // Cache the enum values to not create new values copy
    public static final DataSlotPayloadType[] types = DataSlotPayloadType.values();

    public static final StreamCodec<FriendlyByteBuf, ListEntry> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        ListEntry::index,
        StreamCodec.<FriendlyByteBuf, DataSlotPayload>of(
            (buf, it) -> {
              DataSlotPayloadType type = it.type();
              buf.writeInt(type.ordinal());
              type.getStreamCodec().encode(buf, it);
            },
            (buf) -> {
              int ordinal = buf.readInt();
              DataSlotPayloadType type = types[ordinal];
              return type.getStreamCodec().decode(buf);
            }),
        ListEntry::payload,
        ListEntry::new);

  }

}
