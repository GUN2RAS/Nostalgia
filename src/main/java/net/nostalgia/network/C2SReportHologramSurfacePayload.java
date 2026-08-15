package net.nostalgia.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record C2SReportHologramSurfacePayload(int surfaceY) implements CustomPacketPayload {
  public static final Type<C2SReportHologramSurfacePayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "c2s_report_hologram_surface"));
  public static final StreamCodec<RegistryFriendlyByteBuf, C2SReportHologramSurfacePayload> CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, C2SReportHologramSurfacePayload::surfaceY, C2SReportHologramSurfacePayload::new
  );

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
