package net.nostalgia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import java.util.List;
import java.util.ArrayList;

    public record S2CDimensionSectionsPayload(String dimensionId, List<SectionData> sections, long[] chunkPositions, long[] chunkVersions) implements CustomPacketPayload {

    public record SectionData(int chunkX, int sectionY, int chunkZ, int[] paletteIds, byte[] indices, int[] biomePaletteIds, byte[] biomeIndices) {}

    public static final Type<S2CDimensionSectionsPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath("nostalgia", "s2c_dimension_sections"));

    public static final StreamCodec<FriendlyByteBuf, S2CDimensionSectionsPayload> CODEC = CustomPacketPayload.codec(
        S2CDimensionSectionsPayload::write,
        S2CDimensionSectionsPayload::read
    );

    private static S2CDimensionSectionsPayload read(FriendlyByteBuf buf) {
        String dimensionId = buf.readUtf();
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
                int pos = 0;
                while (pos < 4096) {
                    int rleCount = buf.readVarInt();
                    byte val = buf.readByte();
                    for (int j = 0; j < rleCount; j++) {
                        indices[pos++] = val;
                    }
                }
            }
            
            int biomePalSize = buf.readShort() & 0xFFFF;
            int[] biomePaletteIds = new int[biomePalSize];
            for (int p = 0; p < biomePalSize; p++) {
                biomePaletteIds[p] = buf.readVarInt();
            }
            byte[] biomeIndices = null;
            if (biomePalSize > 1) {
                biomeIndices = new byte[64];
                int pos = 0;
                while (pos < 64) {
                    int rleCount = buf.readVarInt();
                    byte val = buf.readByte();
                    for (int j = 0; j < rleCount; j++) {
                        biomeIndices[pos++] = val;
                    }
                }
            }
            
            sections.add(new SectionData(cx, sy, cz, paletteIds, indices, biomePaletteIds, biomeIndices));
        }
        
        long[] chunkPositions = null;
        long[] chunkVersions = null;
        if (buf.readableBytes() > 0) {
            int vCount = buf.readVarInt();
            if (vCount > 0) {
                chunkPositions = new long[vCount];
                chunkVersions = new long[vCount];
                for (int i = 0; i < vCount; i++) chunkPositions[i] = buf.readLong();
                for (int i = 0; i < vCount; i++) chunkVersions[i] = buf.readLong();
            }
        }
        
        return new S2CDimensionSectionsPayload(dimensionId, sections, chunkPositions, chunkVersions);
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(dimensionId);
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
                int rleCount = 0;
                byte current = s.indices[0];
                for (int j = 0; j < 4096; j++) {
                    if (s.indices[j] == current) {
                        rleCount++;
                    } else {
                        buf.writeVarInt(rleCount);
                        buf.writeByte(current);
                        current = s.indices[j];
                        rleCount = 1;
                    }
                }
                buf.writeVarInt(rleCount);
                buf.writeByte(current);
            }

            buf.writeShort(s.biomePaletteIds.length);
            for (int id : s.biomePaletteIds) {
                buf.writeVarInt(id);
            }
            if (s.biomePaletteIds.length > 1 && s.biomeIndices != null) {
                int rleCount = 0;
                byte current = s.biomeIndices[0];
                for (int j = 0; j < 64; j++) {
                    if (s.biomeIndices[j] == current) {
                        rleCount++;
                    } else {
                        buf.writeVarInt(rleCount);
                        buf.writeByte(current);
                        current = s.biomeIndices[j];
                        rleCount = 1;
                    }
                }
                buf.writeVarInt(rleCount);
                buf.writeByte(current);
            }
        }
        
        if (chunkPositions != null && chunkVersions != null && chunkPositions.length > 0) {
            buf.writeVarInt(chunkPositions.length);
            for (long l : chunkPositions) buf.writeLong(l);
            for (long l : chunkVersions) buf.writeLong(l);
        } else {
            buf.writeVarInt(0);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
