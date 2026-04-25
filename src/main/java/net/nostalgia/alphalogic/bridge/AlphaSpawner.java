package net.nostalgia.alphalogic.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.LightLayer;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.world.dimension.ModDimensions;

public class AlphaSpawner {

    private static final EntityType<?>[] MONSTERS = {
        EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.ZOMBIE, 
        EntityType.SKELETON, EntityType.SKELETON, 
        EntityType.SPIDER, EntityType.SPIDER, 
        EntityType.CREEPER, EntityType.CREEPER
    };
    
    private static final EntityType<?>[] ANIMALS = {
        EntityType.PIG, EntityType.SHEEP, EntityType.COW, EntityType.CHICKEN
    };
    
    public static void performSpawns(ServerLevel level) {
        if (level.dimension() != ModDimensions.ALPHA_112_01_LEVEL_KEY) return;
        
        long time = level.getGameTime();
        if (time % 10 != 0) return; 
        
        RandomSource rand = level.getRandom();
        
        for (ServerPlayer player : level.players()) {
            if (rand.nextInt(2) == 0) { 
                int dx = rand.nextInt(48) - 24;
                int dz = rand.nextInt(48) - 24;
                if (Math.abs(dx) < 16 && Math.abs(dz) < 16) continue; 
                
                int x = player.getBlockX() + dx;
                int z = player.getBlockZ() + dz;
                int y = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY();
                
                BlockPos targetPos = new BlockPos(x, y, z);
                
                int skyLight = level.getBrightness(LightLayer.SKY, targetPos) - level.getSkyDarken();
                boolean isNightOrDark = skyLight <= 4 && level.getBrightness(LightLayer.BLOCK, targetPos) < 8;
                
                if (isNightOrDark) {
                    if (level.getBlockState(targetPos.below()).isSolid()) {
                        EntityType<?> type = MONSTERS[rand.nextInt(MONSTERS.length)];
                        Mob mob = (Mob) type.create(level, EntitySpawnReason.NATURAL);
                        if (mob != null) {
                            mob.setPos(x + 0.5, y, z + 0.5);
                            level.addFreshEntity(mob);
                        }
                    }
                } else {
                    if (level.getBlockState(targetPos.below()).is(AlphaBlocks.ALPHA_GRASS_BLOCK) && skyLight > 8) {
                        if (rand.nextInt(6) == 0) { 
                            EntityType<?> type = ANIMALS[rand.nextInt(ANIMALS.length)];
                            Mob mob = (Mob) type.create(level, EntitySpawnReason.NATURAL);
                            if (mob != null) {
                                mob.setPos(x + 0.5, y, z + 0.5);
                                level.addFreshEntity(mob);
                            }
                        }
                    }
                }
            }
        }
    }
}
