package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

import java.util.UUID;

public record S2CEndTransitionVisualsPayload(UUID instanceId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CEndTransitionVisualsPayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "end_transition_visuals"));

    public static final StreamCodec<FriendlyByteBuf, S2CEndTransitionVisualsPayload> CODEC = CustomPacketPayload.codec(
            S2CEndTransitionVisualsPayload::write,
            S2CEndTransitionVisualsPayload::new
    );

    private S2CEndTransitionVisualsPayload(FriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.instanceId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
