package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record C2SRequestPortalDeltasPayload() implements CustomPacketPayload {
    public static final Type<C2SRequestPortalDeltasPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "c2s_request_portal_deltas"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SRequestPortalDeltasPayload> CODEC = StreamCodec.unit(new C2SRequestPortalDeltasPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
