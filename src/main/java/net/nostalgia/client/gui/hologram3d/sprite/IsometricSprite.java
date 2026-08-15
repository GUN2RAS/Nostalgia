package net.nostalgia.client.gui.hologram3d.sprite;

import java.util.Arrays;

public class IsometricSprite {
  private final int[] topPixels;
  private final int[] sidePixels;
  private final IsometricSprite.LOD lod;

  private IsometricSprite(int[] topPixels, int[] sidePixels, IsometricSprite.LOD lod) {
    this.topPixels = topPixels;
    this.sidePixels = sidePixels;
    this.lod = lod;
  }

  public static IsometricSprite create(int[] srcTop16, int[] srcSide16, IsometricSprite.LOD lod) {
    int[] top = projectTopFace(srcTop16, lod);
    int[] side = projectSideFace(srcSide16, lod);
    return new IsometricSprite(top, side, lod);
  }

  public static IsometricSprite fromColor(int color, IsometricSprite.LOD lod) {
    int[] top = new int[lod.tileW * lod.tileH];
    int[] side = new int[lod.tileW / 2 * lod.sideH];
    Arrays.fill(top, color);
    int r = (color >> 16 & 0xFF) * 6 / 10;
    int g = (color >> 8 & 0xFF) * 6 / 10;
    int b = (color & 0xFF) * 6 / 10;
    int darkColor = 0xFF000000 | r << 16 | g << 8 | b;
    Arrays.fill(side, darkColor);
    return new IsometricSprite(top, side, lod);
  }

  public static IsometricSprite outlineFromColor(int color, IsometricSprite.LOD lod) {
    int w = lod.tileW;
    int h = lod.tileH;
    int[] top = new int[w * h];

    for (int py = 0; py < h; py++) {
      float rowRatio = (float)py / h;
      int rowW = (int)(w * (1.0F - rowRatio * 0.8F));
      int startX = (w - rowW) / 2;
      int endX = startX + rowW - 1;
      if (rowW <= 2) {
        for (int px = startX; px <= endX && px < w; px++) {
          top[py * w + px] = color;
        }
      } else {
        if (startX >= 0 && startX < w) {
          top[py * w + startX] = color;
        }

        if (endX >= 0 && endX < w) {
          top[py * w + endX] = color;
        }
      }
    }

    int sideW = lod.tileW / 2;
    if (sideW < 1) {
      sideW = 1;
    }

    int sideH = lod.sideH;
    int[] side = new int[sideW * sideH];
    int r = (color >> 16 & 0xFF) * 6 / 10;
    int g = (color >> 8 & 0xFF) * 6 / 10;
    int b = (color & 0xFF) * 6 / 10;
    int darkColor = 0xFF000000 | r << 16 | g << 8 | b;

    for (int pyx = 0; pyx < sideH; pyx++) {
      side[pyx * sideW] = darkColor;
      side[pyx * sideW + sideW - 1] = darkColor;
    }

    for (int px = 0; px < sideW; px++) {
      side[(sideH - 1) * sideW + px] = darkColor;
    }

    return new IsometricSprite(top, side, lod);
  }

  private static int[] projectTopFace(int[] src16, IsometricSprite.LOD lod) {
    int w = lod.tileW;
    int h = lod.tileH;
    int[] result = new int[w * h];

    for (int py = 0; py < h; py++) {
      float rowRatio = (float)py / h;
      int rowW = (int)(w * (1.0F - rowRatio * 0.8F));
      int startX = (w - rowW) / 2;

      for (int px = 0; px < rowW; px++) {
        float u = (float)px / rowW;
        int srcX = Math.min(15, (int)(u * 16.0F));
        int srcY = Math.min(15, (int)(rowRatio * 16.0F));
        int color = src16[srcY * 16 + srcX];
        if ((color >> 24 & 0xFF) >= 128) {
          result[py * w + startX + px] = color;
        }
      }
    }

    return result;
  }

  private static int[] projectSideFace(int[] src16, IsometricSprite.LOD lod) {
    int w = lod.tileW / 2;
    if (w < 1) {
      w = 1;
    }

    int h = lod.sideH;
    int[] result = new int[w * h];

    for (int py = 0; py < h; py++) {
      for (int px = 0; px < w; px++) {
        float u = (float)px / w;
        float v = (float)py / h;
        int srcX = Math.min(15, (int)(u * 16.0F));
        int srcY = Math.min(15, (int)((0.5F + v * 0.5F) * 16.0F));
        int color = src16[srcY * 16 + srcX];
        if ((color >> 24 & 0xFF) >= 128) {
          int r = (color >> 16 & 0xFF) * 7 / 10;
          int g = (color >> 8 & 0xFF) * 7 / 10;
          int b = (color & 0xFF) * 7 / 10;
          result[py * w + px] = 0xFF000000 | r << 16 | g << 8 | b;
        }
      }
    }

    return result;
  }

  public int getTopPixel(int localX, int localY) {
    int w = this.lod.tileW;
    return localX >= 0 && localX < w && localY >= 0 && localY < this.lod.tileH ? this.topPixels[localY * w + localX] : 0;
  }

  public int getSidePixel(int localX, int localY) {
    int w = this.lod.tileW / 2;
    if (w < 1) {
      w = 1;
    }

    return localX >= 0 && localX < w && localY >= 0 && localY < this.lod.sideH ? this.sidePixels[localY * w + localX] : 0;
  }

  public IsometricSprite.LOD getLod() {
    return this.lod;
  }

  public static IsometricSprite.LOD lodForZoom(float zoom) {
    if (zoom >= 2.5F) {
      return IsometricSprite.LOD.LARGE;
    } else if (zoom >= 1.0F) {
      return IsometricSprite.LOD.MEDIUM;
    } else {
      return zoom >= 0.4F ? IsometricSprite.LOD.SMALL : IsometricSprite.LOD.TINY;
    }
  }

  public static enum LOD {
    TINY(2, 1, 1),
    SMALL(4, 2, 2),
    MEDIUM(8, 4, 3),
    LARGE(16, 8, 5);

    public final int tileW;
    public final int tileH;
    public final int sideH;

    private LOD(int tileW, int tileH, int sideH) {
      this.tileW = tileW;
      this.tileH = tileH;
      this.sideH = sideH;
    }
  }
}
