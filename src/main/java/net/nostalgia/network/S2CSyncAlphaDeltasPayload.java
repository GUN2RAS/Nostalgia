package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CSyncAlphaDeltasPayload(long[] positions, int[] states) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSyncAlphaDeltasPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_sync_alpha_deltas"));
    
    public static final StreamCodec<FriendlyByteBuf, S2CSyncAlphaDeltasPayload> CODEC = CustomPacketPayload.codec(
            S2CSyncAlphaDeltasPayload::write,
            S2CSyncAlphaDeltasPayload::new
    );

    private S2CSyncAlphaDeltasPayload(FriendlyByteBuf buf) {
        this(readLongArraySafe(buf), readIntArraySafe(buf));
    }

    private static long[] readLongArraySafe(FriendlyByteBuf buf) {
        int length = buf.readVarInt();
        long[] output = new long[length];
        for (int i = 0; i < length; i++) output[i] = buf.readLong();
        return output;
    }

    private static int[] readIntArraySafe(FriendlyByteBuf buf) {
        int length = buf.readVarInt();
        int[] output = new int[length];
        for (int i = 0; i < length; i++) output[i] = buf.readVarInt();
        return output;
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.positions.length);
        for (long pos : this.positions) buf.writeLong(pos);
        
        buf.writeVarInt(this.states.length);
        for (int state : this.states) buf.writeVarInt(state);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
