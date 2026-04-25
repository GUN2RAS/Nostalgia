package net.nostalgia.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class ZoneSoundManager {

    private static boolean wasInZone = false;

    public static void tick() {
        if (!RitualVisualManager.isTransitioning) {
            if (wasInZone) {
                wasInZone = false;
            }
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || RitualVisualManager.ritualCenter == null) return;

        double dist = mc.player.distanceToSqr(
                RitualVisualManager.ritualCenter.getX(),
                mc.player.getY(),
                RitualVisualManager.ritualCenter.getZ()
        );
        double radius = RitualVisualManager.getAlphaRadius();
        boolean inZone = dist <= radius * radius;

        if (inZone && !wasInZone) {
            wasInZone = true;
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CONDUIT_ACTIVATE, 0.8f, 1.0f));
        } else if (!inZone && wasInZone) {
            wasInZone = false;
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CONDUIT_DEACTIVATE, 0.8f, 1.0f));
        }

        if (inZone && Math.random() < 0.05) {
            mc.player.playSound(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD.value(), 0.5f, 0.5f);
        }
    }
}
