package net.nostalgia.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record S2CSyncParticipantsPayload(List<UUID> participants) implements CustomPacketPayload {
  public static final Type<S2CSyncParticipantsPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("nostalgia", "sync_participants"));
  public static final StreamCodec<FriendlyByteBuf, S2CSyncParticipantsPayload> CODEC = StreamCodec.ofMember(
    S2CSyncParticipantsPayload::write, S2CSyncParticipantsPayload::new
  );

  public S2CSyncParticipantsPayload(FriendlyByteBuf buf) {
    this(readList(buf));
  }

  private static List<UUID> readList(FriendlyByteBuf buf) {
    int size = buf.readVarInt();
    List<UUID> list = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {
      list.add(buf.readUUID());
    }

    return list;
  }

  public void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.participants.size());

    for (UUID uuid : this.participants) {
      buf.writeUUID(uuid);
    }
  }

  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
