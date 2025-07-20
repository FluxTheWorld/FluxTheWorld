package com.fluxtheworld.core.network.sync.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum DataSlotPayloadType implements StringRepresentable {
    INT(0, IntDataSlotPayload.STREAM_CODEC);
    // Add other payload types here

    public static final IntFunction<DataSlotPayloadType> BY_ID = ByIdMap.continuous(DataSlotPayloadType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    private final int id;
    private final StreamCodec<? super RegistryFriendlyByteBuf, ? extends DataSlotPayload> streamCodec;

    DataSlotPayloadType(int id, StreamCodec<? super RegistryFriendlyByteBuf, ? extends DataSlotPayload> streamCodec) {
        this.id = id;
        this.streamCodec = streamCodec;
    }

    public int getId() {
        return id;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, ? extends DataSlotPayload> getStreamCodec() {
        return streamCodec;
    }

    public static DataSlotPayloadType byId(int id) {
        return BY_ID.apply(id);
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}