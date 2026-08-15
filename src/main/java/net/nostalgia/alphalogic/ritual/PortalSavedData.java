package net.nostalgia.alphalogic.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class PortalSavedData extends SavedData {
  private static final String DATA_NAME = "nostalgia_sky_portal";
  public final List<PortalSavedData.PortalEntry> portals;
  public static final Codec<PortalSavedData> CODEC = RecordCodecBuilder.create(
    instance -> instance.group(PortalSavedData.PortalEntry.CODEC.listOf().optionalFieldOf("portals", List.of()).forGetter(d -> d.portals)).apply(instance, PortalSavedData::new)
  );

  public PortalSavedData() {
    this.portals = new ArrayList<>();
  }

  public PortalSavedData(List<PortalSavedData.PortalEntry> portals) {
    this.portals = new ArrayList<>(portals);
  }

  public static SavedDataType<PortalSavedData> type() {
    return new SavedDataType(
      Identifier.fromNamespaceAndPath("nostalgia", "nostalgia_sky_portal"), PortalSavedData::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE
    );
  }

  public static PortalSavedData get(ServerLevel level) {
    return (PortalSavedData)level.getDataStorage().computeIfAbsent(type());
  }

  public void savePortal(
    UUID portalId, BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int timerTicks
  ) {
    this.portals.removeIf(e -> e.portalId().equals(portalId) || e.center().equals(center));
    this.portals.add(new PortalSavedData.PortalEntry(portalId, center, crackPlaneY, crackPlaneYTarget, inverted, seed, sourceDimension, targetDimension, timerTicks));
    this.setDirty();
  }

  public void clearPortal(UUID portalId) {
    this.portals.removeIf(e -> e.portalId().equals(portalId));
    this.setDirty();
  }

  public void clearAllPortals() {
    this.portals.clear();
    this.setDirty();
  }

  public record PortalEntry(
    UUID portalId, BlockPos center, int crackPlaneY, int crackPlaneYTarget, boolean inverted, long seed, String sourceDimension, String targetDimension, int timerTicks
  ) {
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
    public static final Codec<PortalSavedData.PortalEntry> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          UUID_CODEC.fieldOf("portalId").forGetter(PortalSavedData.PortalEntry::portalId),
          BlockPos.CODEC.fieldOf("center").forGetter(PortalSavedData.PortalEntry::center),
          Codec.INT.fieldOf("crackPlaneY").forGetter(PortalSavedData.PortalEntry::crackPlaneY),
          Codec.INT.fieldOf("crackPlaneYTarget").forGetter(PortalSavedData.PortalEntry::crackPlaneYTarget),
          Codec.BOOL.fieldOf("inverted").forGetter(PortalSavedData.PortalEntry::inverted),
          Codec.LONG.fieldOf("seed").forGetter(PortalSavedData.PortalEntry::seed),
          Codec.STRING.fieldOf("sourceDimension").forGetter(PortalSavedData.PortalEntry::sourceDimension),
          Codec.STRING.fieldOf("targetDimension").forGetter(PortalSavedData.PortalEntry::targetDimension),
          Codec.INT.fieldOf("timerTicks").forGetter(PortalSavedData.PortalEntry::timerTicks)
        )
        .apply(instance, PortalSavedData.PortalEntry::new)
    );
  }
}
