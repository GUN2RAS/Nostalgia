package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CGlassBreakPayload(boolean active, BlockPos anchor) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CGlassBreakPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.tryParse("nostalgia:glass_break"));

    public static final StreamCodec<FriendlyByteBuf, S2CGlassBreakPayload> CODEC = CustomPacketPayload.codec(
            S2CGlassBreakPayload::write,
            S2CGlassBreakPayload::new
    );

    private S2CGlassBreakPayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readBlockPos());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.active);
        buf.writeBlockPos(this.anchor);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
