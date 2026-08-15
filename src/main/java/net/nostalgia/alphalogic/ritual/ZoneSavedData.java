package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class ZoneSavedData extends SavedData {
  private static final String DATA_NAME = "nostalgia_timestop_zone";
  public final List<ZoneSavedData.ZoneEntry> zones;
  public static final Codec<ZoneSavedData> CODEC = RecordCodecBuilder.create(
    instance -> instance.group(ZoneSavedData.ZoneEntry.CODEC.listOf().optionalFieldOf("zones", List.of()).forGetter(d -> d.zones))
      .apply(instance, ZoneSavedData::new)
  );

  public ZoneSavedData(List<ZoneSavedData.ZoneEntry> zones) {
    this.zones = new ArrayList<>(zones);
  }

  public static SavedDataType<ZoneSavedData> type() {
    return new SavedDataType(
      Identifier.fromNamespaceAndPath("nostalgia", "nostalgia_timestop_zone"),
      () -> new ZoneSavedData(new ArrayList<>()),
      CODEC,
      DataFixTypes.SAVED_DATA_COMMAND_STORAGE
    );
  }

  public static ZoneSavedData get(ServerLevel overworld) {
    return (ZoneSavedData)overworld.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(type());
  }

  public void updateZones(List<ZoneSavedData.ZoneEntry> newZones) {
    this.zones.clear();
    this.zones.addAll(newZones);
    this.setDirty();
  }

  public static void clear(ServerLevel overworld) {
    ZoneSavedData data = get(overworld);
    data.updateZones(new ArrayList<>());
  }

  public record ZoneEntry(BlockPos beaconPos, int radiusChunks, String dimensionId, long snapGameTime, long snapClockTicks, float snapRain, float snapThunder) {
    public static final Codec<ZoneSavedData.ZoneEntry> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          BlockPos.CODEC.fieldOf("beaconPos").forGetter(ZoneSavedData.ZoneEntry::beaconPos),
          Codec.INT.fieldOf("radiusChunks").forGetter(ZoneSavedData.ZoneEntry::radiusChunks),
          Codec.STRING.fieldOf("dimensionId").forGetter(ZoneSavedData.ZoneEntry::dimensionId),
          Codec.LONG.fieldOf("snapGameTime").forGetter(ZoneSavedData.ZoneEntry::snapGameTime),
          Codec.LONG.fieldOf("snapClockTicks").forGetter(ZoneSavedData.ZoneEntry::snapClockTicks),
          Codec.FLOAT.fieldOf("snapRain").forGetter(ZoneSavedData.ZoneEntry::snapRain),
          Codec.FLOAT.fieldOf("snapThunder").forGetter(ZoneSavedData.ZoneEntry::snapThunder)
        )
        .apply(instance, ZoneSavedData.ZoneEntry::new)
    );
  }
}
