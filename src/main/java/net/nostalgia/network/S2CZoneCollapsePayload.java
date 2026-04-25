package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

public record S2CZoneCollapsePayload(int collapseDurationMs) implements CustomPacketPayload {
    public static final Type<S2CZoneCollapsePayload> ID = new Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "zone_collapse"));
    public static final StreamCodec<FriendlyByteBuf, S2CZoneCollapsePayload> CODEC = CustomPacketPayload.codec(
            S2CZoneCollapsePayload::write,
            S2CZoneCollapsePayload::new
    );

    private S2CZoneCollapsePayload(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(this.collapseDurationMs);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
