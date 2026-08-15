package net.nostalgia.client.gui.hologram3d.render;

import com.mojang.blaze3d.platform.NativeImage;
import net.nostalgia.client.gui.hologram3d.sprite.IsometricSprite;

public class IsometricTileRenderer {
  public IsometricTileRenderer() {
  }

  public static void drawBlock(NativeImage target, int screenX, int screenY, IsometricSprite sprite, int[][] zBuffer, int sortKey, int texW, int texH) {
    IsometricSprite.LOD lod = sprite.getLod();
    int tileW = lod.tileW;
    int tileH = lod.tileH;
    int sideH = lod.sideH;
    int halfW = tileW / 2;
    int topStartX = screenX - halfW;

    for (int py = 0; py < tileH; py++) {
      for (int px = 0; px < tileW; px++) {
        int color = sprite.getTopPixel(px, py);
        if (color != 0) {
          setPixelSafe(target, topStartX + px, screenY + py, color, zBuffer, sortKey, texW, texH);
        }
      }
    }

    int sideW = Math.max(1, tileW / 2);

    for (int py = 0; py < sideH; py++) {
      for (int pxx = 0; pxx < sideW; pxx++) {
        int color = sprite.getSidePixel(pxx, py);
        if (color != 0) {
          setPixelSafe(target, screenX + pxx, screenY + tileH + py, color, zBuffer, sortKey, texW, texH);
        }
      }
    }
  }

  private static void setPixelSafe(NativeImage target, int x, int y, int color, int[][] zBuffer, int sortKey, int texW, int texH) {
    if (x >= 0 && x < texW && y >= 0 && y < texH && sortKey <= zBuffer[x][y]) {
      target.setPixel(x, y, color);
      zBuffer[x][y] = sortKey;
    }
  }
}
