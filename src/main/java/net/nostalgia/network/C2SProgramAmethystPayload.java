package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

public record C2SProgramAmethystPayload(int direction) implements CustomPacketPayload {
    public static final Type<C2SProgramAmethystPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "c2s_program_amethyst"));
    
    public static final StreamCodec<FriendlyByteBuf, C2SProgramAmethystPayload> CODEC = CustomPacketPayload.codec(
            C2SProgramAmethystPayload::write,
            C2SProgramAmethystPayload::new
    );

    private C2SProgramAmethystPayload(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(this.direction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
