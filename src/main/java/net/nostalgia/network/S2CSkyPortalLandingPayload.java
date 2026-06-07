package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

public record S2CSkyPortalLandingPayload(double yOffset, boolean invertZ, int portalCenterZ) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSkyPortalLandingPayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "sky_portal_landing"));

    public static final StreamCodec<FriendlyByteBuf, S2CSkyPortalLandingPayload> CODEC = CustomPacketPayload.codec(
            S2CSkyPortalLandingPayload::write,
            S2CSkyPortalLandingPayload::new
    );

    private S2CSkyPortalLandingPayload(FriendlyByteBuf buf) {
        this(buf.readDouble(), buf.readBoolean(), buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.yOffset);
        buf.writeBoolean(this.invertZ);
        buf.writeInt(this.portalCenterZ);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
