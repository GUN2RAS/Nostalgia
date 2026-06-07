package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class PortalSavedData extends SavedData {

    private static final String DATA_NAME = "nostalgia_sky_portal";

    public record PortalEntry(
            BlockPos center,
            int crackPlaneY,
            int crackPlaneYTarget,
            boolean inverted,
            long seed,
            String sourceDimension,
            String targetDimension,
            int timerTicks
    ) {
        public static final Codec<PortalEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("center").forGetter(PortalEntry::center),
                Codec.INT.fieldOf("crackPlaneY").forGetter(PortalEntry::crackPlaneY),
                Codec.INT.fieldOf("crackPlaneYTarget").forGetter(PortalEntry::crackPlaneYTarget),
                Codec.BOOL.fieldOf("inverted").forGetter(PortalEntry::inverted),
                Codec.LONG.fieldOf("seed").forGetter(PortalEntry::seed),
                Codec.STRING.fieldOf("sourceDimension").forGetter(PortalEntry::sourceDimension),
                Codec.STRING.fieldOf("targetDimension").forGetter(PortalEntry::targetDimension),
                Codec.INT.fieldOf("timerTicks").forGetter(PortalEntry::timerTicks)
        ).apply(instance, PortalEntry::new));
    }

    public Optional<PortalEntry> portal;

    public PortalSavedData() {
        this.portal = Optional.empty();
    }

    public PortalSavedData(Optional<PortalEntry> portal) {
        this.portal = portal;
    }

    public static final Codec<PortalSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PortalEntry.CODEC.optionalFieldOf("portal").forGetter(d -> d.portal)
    ).apply(instance, PortalSavedData::new));

    public static SavedDataType<PortalSavedData> type() {
        return new SavedDataType<>(
                Identifier.fromNamespaceAndPath("nostalgia", DATA_NAME),
                PortalSavedData::new,
                CODEC,
                DataFixTypes.SAVED_DATA_COMMAND_STORAGE
        );
    }

    public static PortalSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(type());
    }

    public void savePortal(BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int timerTicks) {
        this.portal = Optional.of(new PortalEntry(center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, timerTicks));
        this.setDirty();
    }

    public void clearPortal() {
        this.portal = Optional.empty();
        this.setDirty();
    }
}
