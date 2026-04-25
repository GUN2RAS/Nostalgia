package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CSkyEventPayload(boolean active, BlockPos center) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSkyEventPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.tryParse("nostalgia:sky_event"));

    public static final StreamCodec<FriendlyByteBuf, S2CSkyEventPayload> CODEC = CustomPacketPayload.codec(
            S2CSkyEventPayload::write,
            S2CSkyEventPayload::new
    );

    private S2CSkyEventPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readBlockPos());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.active);
        buf.writeBlockPos(this.center);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
