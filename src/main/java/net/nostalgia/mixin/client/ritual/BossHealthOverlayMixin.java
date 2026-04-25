package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.entity.Entity;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.client.ritual.ClientFreezeRegions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {

    @Shadow private Map<UUID, net.minecraft.client.gui.components.LerpingBossEvent> events;

    @Inject(method = "shouldDarkenScreen", at = @At("HEAD"), cancellable = true)
    private void nostalgia$shouldDarkenScreen(CallbackInfoReturnable<Boolean> cir) {
        if (this.events.isEmpty()) return;
        if (nostalgia$hasValidBossEvent(true)) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldCreateWorldFog", at = @At("HEAD"), cancellable = true)
    private void nostalgia$shouldCreateWorldFog(CallbackInfoReturnable<Boolean> cir) {
        if (this.events.isEmpty()) return;
        if (nostalgia$hasValidBossEvent(false)) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }

    private boolean nostalgia$hasValidBossEvent(boolean checkDarken) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return false;
        
        boolean playerInZone = ClientFreezeRegions.isLocalPlayerInZone();
        TickRateManagerAccess access = (TickRateManagerAccess) mc.level.tickRateManager();
        boolean hasZones = access != null && access.nostalgia$hasRegions();

        for (Map.Entry<UUID, net.minecraft.client.gui.components.LerpingBossEvent> entry : this.events.entrySet()) {
            net.minecraft.client.gui.components.LerpingBossEvent event = entry.getValue();
            boolean eventActive = checkDarken ? event.shouldDarkenScreen() : event.shouldCreateWorldFog();
            
            if (eventActive) {
                
                if (!hasZones) return true;

                UUID bossUuid = entry.getKey();
                Entity bossEntity = null;
                for (Entity e : ((net.minecraft.client.multiplayer.ClientLevel)mc.level).entitiesForRendering()) {
                    if (e.getUUID().equals(bossUuid)) {
                        bossEntity = e;
                        break;
                    }
                }

                boolean bossInZone = false;
                if (bossEntity != null) {
                    bossInZone = access.nostalgia$isChunkFrozen(mc.level.dimension(), bossEntity.chunkPosition());
                }
                
                
                if (playerInZone && !bossInZone) continue;
                
                
                if (!playerInZone && bossInZone) continue;
                
                
                return true;
            }
        }
        return false;
    }
}
