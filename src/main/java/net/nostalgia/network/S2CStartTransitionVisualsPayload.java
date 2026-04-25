package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CStartTransitionVisualsPayload(String dimensionId, net.minecraft.core.BlockPos beaconPos, net.minecraft.core.BlockPos safeSpawnPos, long seed) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CStartTransitionVisualsPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_start_transition_visuals"));
    public static final StreamCodec<FriendlyByteBuf, S2CStartTransitionVisualsPayload> CODEC = CustomPacketPayload.codec(
            S2CStartTransitionVisualsPayload::write,
            S2CStartTransitionVisualsPayload::new
    );

    private S2CStartTransitionVisualsPayload(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readBlockPos(), buf.readBlockPos(), buf.readLong());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.dimensionId);
        buf.writeBlockPos(this.beaconPos);
        buf.writeBlockPos(this.safeSpawnPos);
        buf.writeLong(this.seed);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
