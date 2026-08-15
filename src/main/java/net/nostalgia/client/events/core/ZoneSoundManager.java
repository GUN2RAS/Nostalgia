package net.nostalgia.client.events.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;

public class ZoneSoundManager {
  private static boolean wasInZone = false;

  public ZoneSoundManager() {
  }

  public static void tick() {
    ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
    if (transition == null) {
      if (wasInZone) {
        wasInZone = false;
      }
    } else {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && transition.ritualCenter() != null) {
        double dist = mc.player.distanceToSqr(transition.ritualCenter().getX(), mc.player.getY(), transition.ritualCenter().getZ());
        double radius = transition.alphaRadius();
        boolean inZone = dist <= radius * radius;
        if (inZone && !wasInZone) {
          wasInZone = true;
          mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CONDUIT_ACTIVATE, 0.8F, 1.0F));
        } else if (!inZone && wasInZone) {
          wasInZone = false;
          mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CONDUIT_DEACTIVATE, 0.8F, 1.0F));
        }

        if (inZone && Math.random() < 0.05) {
          mc.player.playSound((SoundEvent)SoundEvents.AMBIENT_BASALT_DELTAS_MOOD.value(), 0.5F, 0.5F);
        }
      }
    }
  }
}
