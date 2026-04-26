package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record S2CSetTerminalErrorPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSetTerminalErrorPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("nostalgia", "set_terminal_error"));
    public static final StreamCodec<FriendlyByteBuf, S2CSetTerminalErrorPayload> CODEC = StreamCodec.unit(new S2CSetTerminalErrorPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
