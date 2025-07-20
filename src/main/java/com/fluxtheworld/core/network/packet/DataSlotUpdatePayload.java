package com.fluxtheworld.core.network.packet;

import com.fluxtheworld.core.network.sync.payload.DataSlotPayload;
import com.fluxtheworld.core.network.sync.payload.DataSlotPayloadType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DataSlotUpdatePayload(int containerId, short slotIndex, DataSlotPayload payload) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DataSlotUpdatePayload> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation("fluxtheworld", "data_slot_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DataSlotUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            DataSlotUpdatePayload::containerId,
            ByteBufCodecs.SHORT,
            DataSlotUpdatePayload::slotIndex,
            StreamCodec.dispatch(DataSlotPayload::getType, DataSlotPayloadType::getStreamCodec),
            DataSlotUpdatePayload::payload,
            DataSlotUpdatePayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}