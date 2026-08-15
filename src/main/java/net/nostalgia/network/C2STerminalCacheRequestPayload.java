package net.nostalgia.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record C2STerminalCacheRequestPayload(String targetDimension, boolean hasCache, long[] chunks, long[] versions) implements CustomPacketPayload {
  public static final Type<C2STerminalCacheRequestPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "c2s_terminal_cache_request"));
  public static final StreamCodec<RegistryFriendlyByteBuf, C2STerminalCacheRequestPayload> CODEC = StreamCodec.of((buf, payload) -> {
    buf.writeUtf(payload.targetDimension());
    buf.writeBoolean(payload.hasCache());
    if (payload.chunks() != null && payload.versions() != null && payload.chunks().length > 0) {
      buf.writeInt(payload.chunks().length);

      for (long l : payload.chunks()) {
        buf.writeLong(l);
      }

      for (long l : payload.versions()) {
        buf.writeLong(l);
      }
    } else {
      buf.writeInt(0);
    }
  }, buf -> {
    String dim = buf.readUtf();
    boolean hasCache = buf.readBoolean();
    int len = buf.readInt();
    long[] chunks = new long[len];
    long[] versions = new long[len];
    if (len > 0) {
      for (int i = 0; i < len; i++) {
        chunks[i] = buf.readLong();
      }

      for (int i = 0; i < len; i++) {
        versions[i] = buf.readLong();
      }
    }

    return new C2STerminalCacheRequestPayload(dim, hasCache, chunks, versions);
  });

  public C2STerminalCacheRequestPayload(String targetDimension, boolean hasCache) {
    this(targetDimension, hasCache, new long[0], new long[0]);
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
