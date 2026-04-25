package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class ClientLevelAuraMixin {
    
    @Shadow public abstract boolean isClientSide();

    @Inject(method = "getRainLevel", at = @At("RETURN"), cancellable = true)
    private void nostalgia$auraRain(float a, CallbackInfoReturnable<Float> cir) {
        if (!this.isClientSide()) return;
        cir.setReturnValue(nostalgia$calculateAura(false, cir.getReturnValueF()));
    }
    
    @Inject(method = "getThunderLevel", at = @At("RETURN"), cancellable = true)
    private void nostalgia$auraThunder(float a, CallbackInfoReturnable<Float> cir) {
        if (!this.isClientSide()) return;
        cir.setReturnValue(nostalgia$calculateAura(true, cir.getReturnValueF()));
    }
    
    private float nostalgia$calculateAura(boolean isThunder, float baseLevel) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return baseLevel;
        
        TickRateManagerAccess access = (TickRateManagerAccess) mc.level.tickRateManager();
        if (!access.nostalgia$hasRegions()) return baseLevel;
        
        Vec3 playerPos = mc.player.position();
        float finalLevel = baseLevel;
        double closestDist = Double.MAX_VALUE;
        float closestTargetLevel = 0.0F;
        boolean foundZone = false;
        
        for (FreezeRegion region : access.nostalgia$regions()) {
            if (!region.dimension().equals(mc.level.dimension())) continue;
            
            
            net.nostalgia.client.ritual.ClientFreezeRegions.ZoneSnapshot snap = 
                net.nostalgia.client.ritual.ClientFreezeRegions.snapshots.get(region.beaconPos());
            
            float targetLevel = 0.0F;
            if (snap != null) {
                targetLevel = isThunder ? snap.thunder() : snap.rain();
            }
            
            
            double centerX = region.beaconPos().getX();
            double centerZ = region.beaconPos().getZ();
            double radiusBlocks = (region.chunkRadius() * 16.0) + 8.0;
            
            double dx = Math.abs(playerPos.x - centerX);
            double dz = Math.abs(playerPos.z - centerZ);
            
            
            double distToEdgeX = Math.max(0, dx - radiusBlocks);
            double distToEdgeZ = Math.max(0, dz - radiusBlocks);
            double distanceToBoundary = Math.sqrt(distToEdgeX * distToEdgeX + distToEdgeZ * distToEdgeZ);
            
            if (distanceToBoundary < closestDist) {
                closestDist = distanceToBoundary;
                closestTargetLevel = targetLevel;
                foundZone = true;
            }
        }
        
        if (foundZone) {
            
            double FADE_DIST = 5.0;
            if (closestDist <= 0) {
                finalLevel = closestTargetLevel; 
            } else if (closestDist < FADE_DIST) {
                float intensity = (float)(1.0 - (closestDist / FADE_DIST));
                
                finalLevel = baseLevel + (closestTargetLevel - baseLevel) * intensity;
            }
        }
        
        return finalLevel;
    }
}
