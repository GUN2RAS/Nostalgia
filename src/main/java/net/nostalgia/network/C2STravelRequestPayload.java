package net.nostalgia.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record C2STravelRequestPayload(String targetVersion, BlockPos landingOverride) implements CustomPacketPayload {
  public static final Type<C2STravelRequestPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "c2s_travel_request"));
  public static final StreamCodec<FriendlyByteBuf, C2STravelRequestPayload> CODEC = CustomPacketPayload.codec(
    C2STravelRequestPayload::write, C2STravelRequestPayload::read
  );

  public C2STravelRequestPayload(String targetVersion) {
    this(targetVersion, null);
  }

  public static C2STravelRequestPayload read(FriendlyByteBuf buf) {
    String version = buf.readUtf();
    boolean hasLanding = buf.readBoolean();
    BlockPos landing = hasLanding ? buf.readBlockPos() : null;
    return new C2STravelRequestPayload(version, landing);
  }

  public void write(FriendlyByteBuf buf) {
    buf.writeUtf(this.targetVersion);
    buf.writeBoolean(this.landingOverride != null);
    if (this.landingOverride != null) {
      buf.writeBlockPos(this.landingOverride);
    }
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
