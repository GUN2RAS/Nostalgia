package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CPortalDebugPayload(boolean state, boolean inverted, long seed, net.minecraft.core.BlockPos center) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CPortalDebugPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.tryParse("nostalgia:portal_debug"));

    public static final StreamCodec<FriendlyByteBuf, S2CPortalDebugPayload> CODEC = CustomPacketPayload.codec(
            S2CPortalDebugPayload::write,
            S2CPortalDebugPayload::new
    );

    private S2CPortalDebugPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readBoolean(), buf.readLong(), buf.readBlockPos());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.state);
        buf.writeBoolean(this.inverted);
        buf.writeLong(this.seed);
        buf.writeBlockPos(this.center);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
