package net.nostalgia.alphalogic.core;

public class AlphaBlockBehaviors {
  public AlphaBlockBehaviors() {
  }

  public static void onEntityCollidedWithBlock(AlphaEntity entity, int x, int y, int z, int blockId) {
    if (blockId == 30) {
      entity.inWater = false;
      entity.motionX *= 0.25;
      entity.motionY *= 0.05;
      entity.motionZ *= 0.25;
    } else if (blockId == 51) {
      if (!entity.isImmuneToFire) {
        entity.fire++;
        if (entity.fire == 0) {
          entity.fire = 300;
        }
      }
    } else if (blockId == 81 && entity instanceof AlphaPlayer player && player.mcPlayer != null) {
      player.hurt(player.mcPlayer.damageSources().cactus(), 1.0F);
    }
  }
}
