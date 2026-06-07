package net.nostalgia.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record C2SCacheReadyPayload(boolean hasOverworldCache, long[] chunks, long[] versions) implements CustomPacketPayload {

    public C2SCacheReadyPayload(boolean hasOverworldCache) {
        this(hasOverworldCache, new long[0], new long[0]);
    }

    public static final Type<C2SCacheReadyPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "c2s_cache_ready"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SCacheReadyPayload> CODEC = StreamCodec.of(
        (buf, payload) -> {
            buf.writeBoolean(payload.hasOverworldCache());
            if (payload.chunks() != null && payload.versions() != null && payload.chunks().length > 0) {
                buf.writeInt(payload.chunks().length);
                for (long l : payload.chunks()) buf.writeLong(l);
                for (long l : payload.versions()) buf.writeLong(l);
            } else {
                buf.writeInt(0);
            }
        },
        buf -> {
            boolean hasCache = buf.readBoolean();
            int len = buf.readInt();
            long[] chunks = new long[len];
            long[] versions = new long[len];
            if (len > 0) {
                for (int i = 0; i < len; i++) chunks[i] = buf.readLong();
                for (int i = 0; i < len; i++) versions[i] = buf.readLong();
            }
            return new C2SCacheReadyPayload(hasCache, chunks, versions);
        }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
