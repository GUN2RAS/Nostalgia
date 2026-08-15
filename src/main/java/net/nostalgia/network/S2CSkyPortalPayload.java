package net.nostalgia.network;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CSkyPortalPayload(
  UUID portalId,
  boolean state,
  int crackPlaneY,
  int crackPlaneYTarget,
  boolean inverted,
  long seed,
  BlockPos center,
  String sourceDimension,
  String targetDimension,
  boolean restored
) implements CustomPacketPayload {
  public static final Type<S2CSkyPortalPayload> TYPE = new Type(Identifier.tryParse("nostalgia:sky_portal"));
  public static final StreamCodec<FriendlyByteBuf, S2CSkyPortalPayload> CODEC = CustomPacketPayload.codec(S2CSkyPortalPayload::write, S2CSkyPortalPayload::new);

  public S2CSkyPortalPayload(
    UUID portalId, boolean state, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, BlockPos center, String sourceDimension, String targetDimension
  ) {
    this(portalId, state, crackPlaneY, crackPlaneYTarget, inverted, seed, center, sourceDimension, targetDimension, false);
  }

  private S2CSkyPortalPayload(FriendlyByteBuf buf) {
    this(
      buf.readUUID(), buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readLong(), buf.readBlockPos(), buf.readUtf(), buf.readUtf(), buf.readBoolean()
    );
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeUUID(this.portalId);
    buf.writeBoolean(this.state);
    buf.writeInt(this.crackPlaneY);
    buf.writeInt(this.crackPlaneYTarget);
    buf.writeBoolean(this.inverted);
    buf.writeLong(this.seed);
    buf.writeBlockPos(this.center);
    buf.writeUtf(this.sourceDimension);
    buf.writeUtf(this.targetDimension);
    buf.writeBoolean(this.restored);
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
