package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CSkyPortalCancelPayload(boolean cancel) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSkyPortalCancelPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "sky_portal_cancel"));

    public static final StreamCodec<FriendlyByteBuf, S2CSkyPortalCancelPayload> STREAM_CODEC = CustomPacketPayload.codec(
            S2CSkyPortalCancelPayload::write,
            S2CSkyPortalCancelPayload::new
    );

    private S2CSkyPortalCancelPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(cancel);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
