package net.nostalgia.client.events.echo;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;

public class RitualSoundManager {
  private static boolean wasInsideZone = false;

  public RitualSoundManager() {
  }

  public static void tick(Player player) {
    if (player != null && player.level() != null) {
      TimestopZoneManager.ActiveZone currentZone = TimestopZoneManager.findZoneContaining(player.level().dimension(), player.blockPosition());
      boolean isInsideZone = currentZone != null;
      if (isInsideZone && !wasInsideZone) {
        player.level().playSound(player, player.blockPosition(), SoundEvents.CONDUIT_ACTIVATE, SoundSource.AMBIENT, 1.0F, 0.5F);
      } else if (!isInsideZone && wasInsideZone) {
        player.level().playSound(player, player.blockPosition(), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.AMBIENT, 1.0F, 0.5F);
      }

      if (isInsideZone && player.getRandom().nextInt(30) == 0) {
        player.level()
          .playLocalSound(
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.BEACON_AMBIENT,
            SoundSource.AMBIENT,
            0.4F,
            0.6F + player.getRandom().nextFloat() * 0.2F,
            false
          );
      }

      wasInsideZone = isInsideZone;
    } else {
      wasInsideZone = false;
    }
  }

  public static void clear() {
    wasInsideZone = false;
  }
}
