package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CBystanderVisualsPayload(BlockPos center, int offsetX, int offsetY, int offsetZ, String targetDimensionId, int phase) implements CustomPacketPayload {
    public static final Type<S2CBystanderVisualsPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_bystander_visuals"));

    public static final StreamCodec<FriendlyByteBuf, S2CBystanderVisualsPayload> CODEC = CustomPacketPayload.codec(
            S2CBystanderVisualsPayload::write,
            S2CBystanderVisualsPayload::new
    );

    private S2CBystanderVisualsPayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readUtf(), buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.center);
        buf.writeInt(this.offsetX);
        buf.writeInt(this.offsetY);
        buf.writeInt(this.offsetZ);
        buf.writeUtf(this.targetDimensionId);
        buf.writeInt(this.phase);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
