package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

public record C2SSkyPortalLandingRequestPayload(double playerX, double playerY, double playerZ, float yRot, float xRot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SSkyPortalLandingRequestPayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "sky_portal_landing_request"));

    public static final StreamCodec<FriendlyByteBuf, C2SSkyPortalLandingRequestPayload> CODEC = CustomPacketPayload.codec(
            C2SSkyPortalLandingRequestPayload::write,
            C2SSkyPortalLandingRequestPayload::new
    );

    private C2SSkyPortalLandingRequestPayload(FriendlyByteBuf buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.playerX);
        buf.writeDouble(this.playerY);
        buf.writeDouble(this.playerZ);
        buf.writeFloat(this.yRot);
        buf.writeFloat(this.xRot);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
