package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import java.util.List;
import java.util.ArrayList;

public record S2COverworldSectionsPayload(List<SectionData> sections) implements CustomPacketPayload {

    public record SectionData(int chunkX, int sectionY, int chunkZ, int[] paletteIds, byte[] indices) {}

    public static final Type<S2COverworldSectionsPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_overworld_sections"));

    public static final StreamCodec<FriendlyByteBuf, S2COverworldSectionsPayload> CODEC = CustomPacketPayload.codec(
        S2COverworldSectionsPayload::write,
        S2COverworldSectionsPayload::read
    );

    private static S2COverworldSectionsPayload read(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<SectionData> sections = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int cx = buf.readInt();
            int sy = buf.readInt();
            int cz = buf.readInt();
            int palSize = buf.readShort() & 0xFFFF;
            int[] paletteIds = new int[palSize];
            for (int p = 0; p < palSize; p++) {
                paletteIds[p] = buf.readVarInt();
            }
            byte[] indices = null;
            if (palSize > 1) {
                indices = new byte[4096];
                buf.readBytes(indices);
            }
            sections.add(new SectionData(cx, sy, cz, paletteIds, indices));
        }
        return new S2COverworldSectionsPayload(sections);
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeVarInt(sections.size());
        for (SectionData s : sections) {
            buf.writeInt(s.chunkX);
            buf.writeInt(s.sectionY);
            buf.writeInt(s.chunkZ);
            buf.writeShort(s.paletteIds.length);
            for (int id : s.paletteIds) {
                buf.writeVarInt(id);
            }
            if (s.paletteIds.length > 1 && s.indices != null) {
                buf.writeBytes(s.indices);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
