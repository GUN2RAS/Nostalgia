package net.nostalgia.network;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CEndTransitionVisualsPayload(UUID instanceId) implements CustomPacketPayload {
  public static final Type<S2CEndTransitionVisualsPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "end_transition_visuals"));
  public static final StreamCodec<FriendlyByteBuf, S2CEndTransitionVisualsPayload> CODEC = CustomPacketPayload.codec(
    S2CEndTransitionVisualsPayload::write, S2CEndTransitionVisualsPayload::new
  );

  private S2CEndTransitionVisualsPayload(FriendlyByteBuf buf) {
    this(buf.readUUID());
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeUUID(this.instanceId);
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
