package net.nostalgia.client.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.TickRateManager;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;

@Environment(EnvType.CLIENT)
public class ClientFreezeRegions {

    public record ZoneSnapshot(long gameTime, long clockTicks, float rain, float thunder) {}
    public static final java.util.concurrent.ConcurrentHashMap<BlockPos, ZoneSnapshot> snapshots = new java.util.concurrent.ConcurrentHashMap<>();

    public static TickRateManagerAccess access() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return null;
        TickRateManager mgr = level.tickRateManager();
        if (mgr instanceof TickRateManagerAccess a) return a;
        return null;
    }

    public static void clearAll() {
        snapshots.clear();
        TickRateManagerAccess a = access();
        if (a != null) a.nostalgia$clearRegions();
    }

    public static boolean hasRegions() {
        TickRateManagerAccess a = access();
        return a != null && a.nostalgia$hasRegions();
    }

    public static boolean isLocalPlayerInZone() {
        Minecraft mc = Minecraft.getInstance();
        TickRateManagerAccess a = access();
        if (a == null || mc.player == null || mc.level == null) return false;
        return a.nostalgia$isChunkFrozen(mc.level.dimension(), mc.player.chunkPosition());
    }

    public static boolean isRitualBeacon(BlockPos pos) {
        if (pos == null) return false;
        Minecraft mc = Minecraft.getInstance();
        TickRateManagerAccess a = access();
        if (a == null || mc.level == null) return false;
        net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim = mc.level.dimension();
        for (FreezeRegion r : a.nostalgia$regions()) {
            if (!r.dimension().equals(dim)) continue;
            if (r.beaconPos().equals(pos)) return true;
        }
        return false;
    }
}
