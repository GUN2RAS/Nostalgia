package net.nostalgia.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CRitualPhasePayload(int phase) implements CustomPacketPayload {
    public static final Type<S2CRitualPhasePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_ritual_phase"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CRitualPhasePayload> CODEC = StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.VAR_INT, S2CRitualPhasePayload::phase,
            S2CRitualPhasePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
