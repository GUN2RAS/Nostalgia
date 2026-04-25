package net.nostalgia.alphalogic.bridge;

import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.alphalogic.core.AlphaPlayer;
import net.nostalgia.alphalogic.core.AlphaWorld;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AlphaEngineManager {
    
    private static final Map<UUID, AlphaPlayer> TRACKED_PLAYERS = new ConcurrentHashMap<>();
    private static final Map<Level, AlphaWorld> WRAPPED_WORLDS = new ConcurrentHashMap<>();
    
    private static long worldSeed = 11201L;

    public static void setWorldSeed(long seed) {
        worldSeed = seed;
    }

    public static long getWorldSeed() {
        return worldSeed;
    }

    public static AlphaWorld getWrappedWorld(Level level) {
        if (!WRAPPED_WORLDS.containsKey(level)) {
            
            WRAPPED_WORLDS.put(level, new AlphaWorld(level)); 
        }
        return WRAPPED_WORLDS.get(level);
    }

    public static AlphaPlayer getAlphaPlayer(Player mcPlayer) {
        UUID uuid = mcPlayer.getUUID();
        if (!TRACKED_PLAYERS.containsKey(uuid)) {
            AlphaWorld alphaWorld = getWrappedWorld(mcPlayer.level());
            AlphaPlayer alphaP = new AlphaPlayer(alphaWorld);
            alphaP.setPosition(mcPlayer.getX(), mcPlayer.getY() + alphaP.yOffset - alphaP.ySize, mcPlayer.getZ());
            alphaP.rotationYaw = mcPlayer.getYRot();
            alphaP.rotationPitch = mcPlayer.getXRot();
            alphaWorld.spawnEntityInWorld(alphaP);
            TRACKED_PLAYERS.put(uuid, alphaP);
        }
        return TRACKED_PLAYERS.get(uuid);
    }

    public static void clearPlayer(Player player) {
        TRACKED_PLAYERS.remove(player.getUUID());
    }
}
