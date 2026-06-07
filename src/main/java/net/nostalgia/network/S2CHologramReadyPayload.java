package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CHologramReadyPayload(String dimensionId, BlockPos center, int radius) implements CustomPacketPayload {

    public static final Type<S2CHologramReadyPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_hologram_ready"));

    public static final StreamCodec<FriendlyByteBuf, S2CHologramReadyPayload> CODEC = CustomPacketPayload.codec(
        S2CHologramReadyPayload::write,
        S2CHologramReadyPayload::read
    );

    private static S2CHologramReadyPayload read(FriendlyByteBuf buf) {
        return new S2CHologramReadyPayload(buf.readUtf(), buf.readBlockPos(), buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(dimensionId);
        buf.writeBlockPos(center);
        buf.writeInt(radius);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
