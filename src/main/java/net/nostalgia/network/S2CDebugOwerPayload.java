package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CDebugOwerPayload(boolean active, BlockPos center) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CDebugOwerPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.tryParse("nostalgia:debug_ower"));

    public static final StreamCodec<FriendlyByteBuf, S2CDebugOwerPayload> CODEC = CustomPacketPayload.codec(
            S2CDebugOwerPayload::write,
            S2CDebugOwerPayload::new
    );

    private S2CDebugOwerPayload(FriendlyByteBuf buf) {
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
