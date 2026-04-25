package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

public record C2SBoatCrashPayload(int boatId, double x, double y, double z) implements CustomPacketPayload {
    public static final Type<C2SBoatCrashPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "c2s_boat_crash"));
    public static final StreamCodec<FriendlyByteBuf, C2SBoatCrashPayload> CODEC = CustomPacketPayload.codec(
            C2SBoatCrashPayload::write,
            C2SBoatCrashPayload::new
    );

    private C2SBoatCrashPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(this.boatId);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
