package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.UUID;

public record S2CRitualPhasePayload(UUID instanceId, int phase) implements CustomPacketPayload {
    public static final Type<S2CRitualPhasePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_ritual_phase"));

    public static final StreamCodec<FriendlyByteBuf, S2CRitualPhasePayload> CODEC = CustomPacketPayload.codec(
            S2CRitualPhasePayload::write,
            S2CRitualPhasePayload::new
    );

    private S2CRitualPhasePayload(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readVarInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.instanceId);
        buf.writeVarInt(this.phase);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
