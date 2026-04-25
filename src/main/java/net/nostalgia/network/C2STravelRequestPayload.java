package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record C2STravelRequestPayload(String targetVersion) implements CustomPacketPayload {
    public static final Type<C2STravelRequestPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "c2s_travel_request"));
    public static final StreamCodec<FriendlyByteBuf, C2STravelRequestPayload> CODEC = CustomPacketPayload.codec(
            C2STravelRequestPayload::write,
            C2STravelRequestPayload::new
    );

    public C2STravelRequestPayload(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.targetVersion);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
