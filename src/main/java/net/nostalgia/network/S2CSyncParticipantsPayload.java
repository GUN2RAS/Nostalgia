package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record S2CSyncParticipantsPayload(List<UUID> participants) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSyncParticipantsPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "sync_participants"));

    public static final StreamCodec<FriendlyByteBuf, S2CSyncParticipantsPayload> CODEC = StreamCodec.ofMember(
            S2CSyncParticipantsPayload::write,
            S2CSyncParticipantsPayload::new
    );

    public S2CSyncParticipantsPayload(FriendlyByteBuf buf) {
        this(readList(buf));
    }

    private static List<UUID> readList(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<UUID> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readUUID());
        }
        return list;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(participants.size());
        for (UUID uuid : participants) {
            buf.writeUUID(uuid);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
