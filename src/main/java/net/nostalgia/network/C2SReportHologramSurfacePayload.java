package net.nostalgia.network;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
public record C2SReportHologramSurfacePayload(int surfaceY) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SReportHologramSurfacePayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "c2s_report_hologram_surface"));
    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, C2SReportHologramSurfacePayload> CODEC = StreamCodec.composite(
        net.minecraft.network.codec.ByteBufCodecs.INT, C2SReportHologramSurfacePayload::surfaceY,
        C2SReportHologramSurfacePayload::new
    );
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
}
