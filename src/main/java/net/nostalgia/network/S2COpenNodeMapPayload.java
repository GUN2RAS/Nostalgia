package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2COpenNodeMapPayload() implements CustomPacketPayload {
    public static final Type<S2COpenNodeMapPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_open_node_map"));
    public static final StreamCodec<FriendlyByteBuf, S2COpenNodeMapPayload> CODEC = CustomPacketPayload.codec(
            S2COpenNodeMapPayload::write,
            S2COpenNodeMapPayload::new
    );

    public S2COpenNodeMapPayload(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
        
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
