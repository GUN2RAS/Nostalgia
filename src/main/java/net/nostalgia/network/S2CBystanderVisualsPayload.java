package net.nostalgia.network;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CBystanderVisualsPayload(UUID instanceId, BlockPos center, int offsetX, int offsetY, int offsetZ, String targetDimensionId, int phase)
  implements CustomPacketPayload {
  public static final Type<S2CBystanderVisualsPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "s2c_bystander_visuals"));
  public static final StreamCodec<FriendlyByteBuf, S2CBystanderVisualsPayload> CODEC = CustomPacketPayload.codec(
    S2CBystanderVisualsPayload::write, S2CBystanderVisualsPayload::new
  );

  private S2CBystanderVisualsPayload(FriendlyByteBuf buf) {
    this(buf.readUUID(), buf.readBlockPos(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readUtf(), buf.readInt());
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeUUID(this.instanceId);
    buf.writeBlockPos(this.center);
    buf.writeInt(this.offsetX);
    buf.writeInt(this.offsetY);
    buf.writeInt(this.offsetZ);
    buf.writeUtf(this.targetDimensionId);
    buf.writeInt(this.phase);
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
