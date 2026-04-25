package net.nostalgia.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record C2SCacheReadyPayload() implements CustomPacketPayload {
    public static final Type<C2SCacheReadyPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "c2s_cache_ready"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SCacheReadyPayload> CODEC = StreamCodec.unit(new C2SCacheReadyPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
