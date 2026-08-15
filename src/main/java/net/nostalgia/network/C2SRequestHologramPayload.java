package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record C2SRequestHologramPayload(String dimensionId, BlockPos center, long[] chunks, long[] versions) implements CustomPacketPayload {
    public static final Type<C2SRequestHologramPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "c2s_request_hologram"));

    public C2SRequestHologramPayload(String dimensionId, BlockPos center) {
        this(dimensionId, center, new long[0], new long[0]);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SRequestHologramPayload> CODEC = StreamCodec.of(
        (buf, payload) -> {
            buf.writeUtf(payload.dimensionId());
            buf.writeBlockPos(payload.center());
            buf.writeVarInt(payload.chunks().length);
            for (long l : payload.chunks()) buf.writeLong(l);
            for (long l : payload.versions()) buf.writeLong(l);
        },
        buf -> {
            String dim = buf.readUtf();
            BlockPos pos = buf.readBlockPos();
            int len = buf.readVarInt();
            long[] ch = new long[len];
            long[] ver = new long[len];
            for (int i = 0; i < len; i++) ch[i] = buf.readLong();
            for (int i = 0; i < len; i++) ver[i] = buf.readLong();
            return new C2SRequestHologramPayload(dim, pos, ch, ver);
        }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
