package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CSkyPortalLandingPayload(double yOffset, boolean invertZ, int portalCenterZ) implements CustomPacketPayload {
  public static final Type<S2CSkyPortalLandingPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "sky_portal_landing"));
  public static final StreamCodec<FriendlyByteBuf, S2CSkyPortalLandingPayload> CODEC = CustomPacketPayload.codec(
    S2CSkyPortalLandingPayload::write, S2CSkyPortalLandingPayload::new
  );

  private S2CSkyPortalLandingPayload(FriendlyByteBuf buf) {
    this(buf.readDouble(), buf.readBoolean(), buf.readInt());
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeDouble(this.yOffset);
    buf.writeBoolean(this.invertZ);
    buf.writeInt(this.portalCenterZ);
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
