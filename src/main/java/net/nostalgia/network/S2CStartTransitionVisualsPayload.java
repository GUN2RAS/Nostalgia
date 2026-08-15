package net.nostalgia.network;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CStartTransitionVisualsPayload(
  UUID instanceId,
  String dimensionId,
  BlockPos beaconPos,
  BlockPos safeSpawnPos,
  int offsetX,
  int offsetY,
  int offsetZ,
  long seed,
  int targetSkyColor,
  int targetFogColor,
  int beaconStateId,
  int anchorStateId
) implements CustomPacketPayload {
  public static final Type<S2CStartTransitionVisualsPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "s2c_start_transition_visuals"));
  public static final StreamCodec<FriendlyByteBuf, S2CStartTransitionVisualsPayload> CODEC = CustomPacketPayload.codec(
    S2CStartTransitionVisualsPayload::write, S2CStartTransitionVisualsPayload::new
  );

  private S2CStartTransitionVisualsPayload(FriendlyByteBuf buf) {
    this(
      buf.readUUID(),
      buf.readUtf(),
      buf.readBlockPos(),
      buf.readBlockPos(),
      buf.readInt(),
      buf.readInt(),
      buf.readInt(),
      buf.readLong(),
      buf.readInt(),
      buf.readInt(),
      buf.readInt(),
      buf.readInt()
    );
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

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
