package net.nostalgia.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;

public record S2CEndTransitionVisualsPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CEndTransitionVisualsPayload> TYPE = 
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, "end_transition_visuals"));
    
    public static final StreamCodec<ByteBuf, S2CEndTransitionVisualsPayload> CODEC = 
        StreamCodec.unit(new S2CEndTransitionVisualsPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
