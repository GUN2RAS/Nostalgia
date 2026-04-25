package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ZoneSavedData extends SavedData {

    private static final String DATA_NAME = "nostalgia_timestop_zone";

    public record ZoneEntry(
            BlockPos beaconPos,
            int radiusChunks,
            String dimensionId,
            long snapGameTime,
            long snapClockTicks,
            float snapRain,
            float snapThunder
    ) {
        public static final Codec<ZoneEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("beaconPos").forGetter(ZoneEntry::beaconPos),
                Codec.INT.fieldOf("radiusChunks").forGetter(ZoneEntry::radiusChunks),
                Codec.STRING.fieldOf("dimensionId").forGetter(ZoneEntry::dimensionId),
                Codec.LONG.fieldOf("snapGameTime").forGetter(ZoneEntry::snapGameTime),
                Codec.LONG.fieldOf("snapClockTicks").forGetter(ZoneEntry::snapClockTicks),
                Codec.FLOAT.fieldOf("snapRain").forGetter(ZoneEntry::snapRain),
                Codec.FLOAT.fieldOf("snapThunder").forGetter(ZoneEntry::snapThunder)
        ).apply(instance, ZoneEntry::new));
    }

    public final List<ZoneEntry> zones;

    public ZoneSavedData(List<ZoneEntry> zones) {
        this.zones = new java.util.ArrayList<>(zones);
    }

    public static final Codec<ZoneSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ZoneEntry.CODEC.listOf().optionalFieldOf("zones", List.of()).forGetter(d -> d.zones)
    ).apply(instance, ZoneSavedData::new));

    public static SavedDataType<ZoneSavedData> type() {
        return new SavedDataType<>(
                Identifier.fromNamespaceAndPath("nostalgia", DATA_NAME),
                () -> new ZoneSavedData(new java.util.ArrayList<>()),
                CODEC,
                DataFixTypes.SAVED_DATA_COMMAND_STORAGE
        );
    }

    public static ZoneSavedData get(ServerLevel overworld) {
        return overworld.getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD).getDataStorage().computeIfAbsent(type());
    }

    public void updateZones(List<ZoneEntry> newZones) {
        this.zones.clear();
        this.zones.addAll(newZones);
        this.setDirty();
    }

    public static void clear(ServerLevel overworld) {
        ZoneSavedData data = get(overworld);
        data.updateZones(new java.util.ArrayList<>());
    }
}
