package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CWorldSeedPayload(long seed) implements CustomPacketPayload {
  public static final Type<S2CWorldSeedPayload> TYPE = new Type(Identifier.tryParse("nostalgia:world_seed"));
  public static final StreamCodec<FriendlyByteBuf, S2CWorldSeedPayload> CODEC = CustomPacketPayload.codec(S2CWorldSeedPayload::write, S2CWorldSeedPayload::new);

  private S2CWorldSeedPayload(FriendlyByteBuf buf) {
    this(buf.readLong());
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeLong(this.seed);
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
