package com.fluxtheworld.core.slot;

import java.util.List;

import com.fluxtheworld.FTW;
import com.fluxtheworld.core.Preconditions;
import com.fluxtheworld.core.menu.GenericMenu;
import com.fluxtheworld.core.slot.payload.DataSlotPayload;
import com.fluxtheworld.core.slot.payload.DataSlotPayloadType;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public record SyncDataSlotsPacket(int containerId, List<ListEntry> entries) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SyncDataSlotsPacket> TYPE = new CustomPacketPayload.Type<>(FTW.loc("sync_data_slots"));

  public static final StreamCodec<FriendlyByteBuf, SyncDataSlotsPacket> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT,
      SyncDataSlotsPacket::containerId,
      ListEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
      SyncDataSlotsPacket::entries,
      SyncDataSlotsPacket::new);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

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

  public static class Handler {
    private Handler() {
    }

    public static void register(IEventBus eventBus) {
      eventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(SyncDataSlotsPacket.TYPE, SyncDataSlotsPacket.STREAM_CODEC,
            new DirectionalPayloadHandler<>(Handler::handleClientSide, Handler::handleServerSide));
      });
    }

    public static void handleClientSide(SyncDataSlotsPacket packet, IPayloadContext context) {
      Preconditions.ensureSide(LogicalSide.CLIENT);

      context.enqueueWork(() -> {
        if (context.player().containerMenu.containerId != packet.containerId()) {
          return;
        }

        if (context.player().containerMenu instanceof GenericMenu menu) {
          menu.handleSyncDataSlotsPacket(packet);
        }
      });
    }

    public static void handleServerSide(SyncDataSlotsPacket packet, IPayloadContext context) {
      Preconditions.ensureSide(LogicalSide.SERVER);

      context.enqueueWork(() -> {
        if (context.player().containerMenu.containerId != packet.containerId()) {
          return;
        }

        if (context.player().containerMenu instanceof GenericMenu menu) {
          menu.handleSyncDataSlotsPacket(packet);
        }
      });
    }
  }
}
