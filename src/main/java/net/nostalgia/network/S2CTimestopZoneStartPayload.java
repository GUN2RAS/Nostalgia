package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CTimestopZoneStartPayload(
        BlockPos beaconPos,
        int radiusChunks,
        String dimensionId,
        boolean instant,
        long snapGameTime,
        long snapClockTicks,
        float snapRain,
        float snapThunder
) implements CustomPacketPayload {
    public static final Type<S2CTimestopZoneStartPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_timestop_zone_start"));

    public static final StreamCodec<FriendlyByteBuf, S2CTimestopZoneStartPayload> CODEC = CustomPacketPayload.codec(
            S2CTimestopZoneStartPayload::write,
            S2CTimestopZoneStartPayload::new
    );

    private S2CTimestopZoneStartPayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readVarInt(), buf.readUtf(),
                buf.readBoolean(), buf.readLong(), buf.readLong(), buf.readFloat(), buf.readFloat());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(beaconPos);
        buf.writeVarInt(radiusChunks);
        buf.writeUtf(dimensionId);
        buf.writeBoolean(instant);
        buf.writeLong(snapGameTime);
        buf.writeLong(snapClockTicks);
        buf.writeFloat(snapRain);
        buf.writeFloat(snapThunder);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
