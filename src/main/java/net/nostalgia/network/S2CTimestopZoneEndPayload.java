package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CTimestopZoneEndPayload(BlockPos beaconPos) implements CustomPacketPayload {
    public static final Type<S2CTimestopZoneEndPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_timestop_zone_end"));

    public static final StreamCodec<FriendlyByteBuf, S2CTimestopZoneEndPayload> CODEC = CustomPacketPayload.codec(
            S2CTimestopZoneEndPayload::write,
            S2CTimestopZoneEndPayload::new
    );

    private S2CTimestopZoneEndPayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(beaconPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
