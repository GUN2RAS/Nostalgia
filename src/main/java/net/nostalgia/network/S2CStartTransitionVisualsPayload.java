package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.UUID;

public record S2CStartTransitionVisualsPayload(UUID instanceId, String dimensionId, net.minecraft.core.BlockPos beaconPos, net.minecraft.core.BlockPos safeSpawnPos, int offsetX, int offsetY, int offsetZ, long seed, int targetSkyColor, int targetFogColor, int beaconStateId, int anchorStateId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CStartTransitionVisualsPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_start_transition_visuals"));
    public static final StreamCodec<FriendlyByteBuf, S2CStartTransitionVisualsPayload> CODEC = CustomPacketPayload.codec(
            S2CStartTransitionVisualsPayload::write,
            S2CStartTransitionVisualsPayload::new
    );

    private S2CStartTransitionVisualsPayload(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf(), buf.readBlockPos(), buf.readBlockPos(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readLong(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.instanceId);
        buf.writeUtf(this.dimensionId);
        buf.writeBlockPos(this.beaconPos);
        buf.writeBlockPos(this.safeSpawnPos);
        buf.writeInt(this.offsetX);
        buf.writeInt(this.offsetY);
        buf.writeInt(this.offsetZ);
        buf.writeLong(this.seed);
        buf.writeInt(this.targetSkyColor);
        buf.writeInt(this.targetFogColor);
        buf.writeInt(this.beaconStateId);
        buf.writeInt(this.anchorStateId);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
