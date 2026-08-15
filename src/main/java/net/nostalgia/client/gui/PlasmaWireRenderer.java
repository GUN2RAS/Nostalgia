package net.nostalgia.client.gui;

import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PlasmaWireRenderer {
  public static void drawWireBase(GuiGraphicsExtractor graphics, PlasmaWireRenderer.WirePath path, int x, int y) {
    int wGutter = path.width + 2;
    int baseColor = -15463159;
    int highlightColor = 587202559;

    for (PlasmaWireRenderer.WireSegment s : path.segments) {
      int x1 = x + s.x1;
      int y1 = y + s.y1;
      int x2 = x + s.x2;
      int y2 = y + s.y2;
      int minX = Math.min(x1, x2);
      int maxX = Math.max(x1, x2);
      int minY = Math.min(y1, y2);
      int maxY = Math.max(y1, y2);
      if (s.isVertical) {
        graphics.fill(minX - wGutter / 2, minY, minX - wGutter / 2 + wGutter, maxY + 1, baseColor);
        graphics.fill(minX + wGutter / 2, minY, minX + wGutter / 2 + 1, maxY + 1, highlightColor);
        graphics.fill(minX - wGutter / 2 - 1, minY, minX - wGutter / 2, maxY + 1, 1140850688);
      } else {
        graphics.fill(minX, minY - wGutter / 2, maxX + 1, minY - wGutter / 2 + wGutter, baseColor);
        graphics.fill(minX, minY + wGutter / 2, maxX + 1, minY + wGutter / 2 + 1, highlightColor);
        graphics.fill(minX, minY - wGutter / 2 - 1, maxX + 1, minY - wGutter / 2, 1140850688);
      }
    }
  }

  public static void drawWirePathRange(GuiGraphicsExtractor graphics, PlasmaWireRenderer.WirePath path, int x, int y, int d1, int d2, int width, int color) {
    if (d1 < d2) {
      int currentDist = 0;

      for (PlasmaWireRenderer.WireSegment s : path.segments) {
        int sEnd = currentDist + s.length;
        int s1 = Math.max(d1, currentDist);
        int s2 = Math.min(d2, sEnd);
        if (s1 < s2) {
          int offset1 = s1 - currentDist;
          int offset2 = s2 - currentDist;
          if (s.isVertical) {
            int px = x + s.x1;
            int py1 = y + s.y1 + (s.y2 >= s.y1 ? offset1 : -offset1);
            int py2 = y + s.y1 + (s.y2 >= s.y1 ? offset2 : -offset2);
            int minY = Math.min(py1, py2);
            int maxY = Math.max(py1, py2);
            graphics.fill(px - width / 2, minY, px - width / 2 + width, maxY + 1, color);
          } else {
            int py = y + s.y1;
            int px1 = x + s.x1 + (s.x2 >= s.x1 ? offset1 : -offset1);
            int px2 = x + s.x1 + (s.x2 >= s.x1 ? offset2 : -offset2);
            int minX = Math.min(px1, px2);
            int maxX = Math.max(px1, px2);
            graphics.fill(minX, py - width / 2, maxX + 1, py - width / 2 + width, color);
          }
        }

        currentDist += s.length;
      }
    }
  }

  public static void drawCableLightning(GuiGraphicsExtractor graphics, PlasmaWireRenderer.WirePath path, int x, int y, float time, int color) {
    long interval = (long)(time / 150.0F);
    Random rand = new Random(interval * 31L + path.hashCode());
    if (!(rand.nextFloat() > 0.4F)) {
      int segIdx = rand.nextInt(path.segments.size());
      PlasmaWireRenderer.WireSegment s = path.segments.get(segIdx);
      int px1 = x + s.x1;
      int py1 = y + s.y1;
      int px2 = x + s.x2;
      int py2 = y + s.y2;
      int curX = px1;
      int curY = py1;
      int steps = Math.max(3, s.length / 8);

      for (int i = 1; i <= steps; i++) {
        float t = (float)i / steps;
        int targetX = (int)(px1 + (px2 - px1) * t);
        int targetY = (int)(py1 + (py2 - py1) * t);
        if (i < steps) {
          int offset = rand.nextInt(5) - 2;
          if (s.isVertical) {
            targetX += offset;
          } else {
            targetY += offset;
          }
        }

        int minX = Math.min(curX, targetX);
        int maxX = Math.max(curX, targetX);
        int minY = Math.min(curY, targetY);
        int maxY = Math.max(curY, targetY);
        int haloCol = 2013265920 | color & 16777215;
        graphics.fill(minX - 1, minY - 1, maxX + 2, maxY + 2, haloCol);
        graphics.fill(minX, minY, maxX + 1, maxY + 1, -1);
        curX = targetX;
        curY = targetY;
      }
    }
  }

  public static void drawSlotLightning(GuiGraphicsExtractor graphics, int x, int y, float time, int color, float overloadProgress) {
    long interval = (long)(time / 80.0F);
    Random rand = new Random(interval * 17L);
    int numArcs = rand.nextInt(2) + 1;
    if (overloadProgress > 0.6F) {
      numArcs = rand.nextInt(3) + 2;
    }

    for (int a = 0; a < numArcs; a++) {
      int side = rand.nextInt(4);
      int sx = x + 80;
      int sy = y + 116;
      if (side == 0) {
        sx += rand.nextInt(18);
      } else if (side == 1) {
        sx += rand.nextInt(18);
        sy += 18;
      } else if (side == 2) {
        sy += rand.nextInt(18);
      } else {
        sx += 18;
        sy += rand.nextInt(18);
      }

      int angle = rand.nextInt(360);
      double rad = Math.toRadians(angle);
      int dist = rand.nextInt(12) + 6;
      int ex = sx + (int)(Math.cos(rad) * dist);
      int ey = sy + (int)(Math.sin(rad) * dist);
      int curX = sx;
      int curY = sy;
      int steps = 3;

      for (int i = 1; i <= steps; i++) {
        float t = (float)i / steps;
        int tx = (int)(sx + (ex - sx) * t + (rand.nextFloat() - 0.5F) * 4.0F);
        int ty = (int)(sy + (ey - sy) * t + (rand.nextFloat() - 0.5F) * 4.0F);
        if (i == steps) {
          tx = ex;
          ty = ey;
        }

        int minX = Math.min(curX, tx);
        int maxX = Math.max(curX, tx);
        int minY = Math.min(curY, ty);
        int maxY = Math.max(curY, ty);
        graphics.fill(minX - 1, minY - 1, maxX + 2, maxY + 2, 1677721600 | color & 16777215);
        graphics.fill(minX, minY, maxX + 1, maxY + 1, -1);
        curX = tx;
        curY = ty;
      }
    }
  }

  public static void drawPlasmaWire(
    GuiGraphicsExtractor graphics,
    PlasmaWireRenderer.WirePath path,
    int x,
    int y,
    float time,
    float speed,
    int color,
    float flowProgress,
    float overloadProgress,
    boolean isOverloading
  ) {
    int r = color >> 16 & 0xFF;
    int g = color >> 8 & 0xFF;
    int b = color & 0xFF;
    int maxDist = (int)(path.totalLength * flowProgress);
    if (maxDist > 0) {
      float alphaOuterVal = isOverloading
        ? 0.12F + overloadProgress * 0.15F + 0.04F * (float)Math.sin(time * 0.04F)
        : 0.08F + 0.03F * (float)Math.sin(time * 0.002F);
      int alphaOuter = Math.max(0, Math.min(255, (int)(alphaOuterVal * 255.0F)));
      int colOuter = alphaOuter << 24 | r << 16 | g << 8 | b;
      int wOuter = path.width + 4 + (int)(overloadProgress * 2.0F);
      drawWirePathRange(graphics, path, x, y, 0, maxDist, wOuter, colOuter);
      float alphaInnerVal = isOverloading
        ? 0.25F + overloadProgress * 0.3F + 0.08F * (float)Math.sin(time * 0.04F)
        : 0.22F + 0.05F * (float)Math.sin(time * 0.002F);
      int alphaInner = Math.max(0, Math.min(255, (int)(alphaInnerVal * 255.0F)));
      int colInner = alphaInner << 24 | r << 16 | g << 8 | b;
      int wInner = path.width + 2 + (int)(overloadProgress * 1.0F);
      drawWirePathRange(graphics, path, x, y, 0, maxDist, wInner, colInner);
      float alphaBodyVal = isOverloading ? 1.0F : 0.75F;
      int alphaBody = (int)(alphaBodyVal * 255.0F);
      int colBody = alphaBody << 24 | r << 16 | g << 8 | b;
      drawWirePathRange(graphics, path, x, y, 0, maxDist, path.width, colBody);
      int accumulatedDist = 0;

      for (PlasmaWireRenderer.WireSegment s : path.segments) {
        if (accumulatedDist >= maxDist) {
          break;
        }

        int sStart = accumulatedDist;
        int sEnd = accumulatedDist + s.length;
        int limit = Math.min(maxDist, sEnd);

        for (int d = accumulatedDist; d < limit; d += 2) {
          int segmentDist = d - sStart;
          int px;
          int py;
          if (s.isVertical) {
            px = x + s.x1;
            py = y + s.y1 + (s.y2 >= s.y1 ? segmentDist : -segmentDist);
          } else {
            px = x + s.x1 + (s.x2 >= s.x1 ? segmentDist : -segmentDist);
            py = y + s.y1;
          }

          float phase1 = d * 0.15F - time * speed;
          float phase2 = d * 0.35F - time * speed * 1.6F;
          float wave1 = (float)Math.sin(phase1) * 0.45F + 0.55F;
          float wave2 = (float)Math.sin(phase2) * 0.25F + 0.25F;
          float intensity = wave1 + wave2;
          if (isOverloading) {
            intensity *= 1.0F + overloadProgress * 0.8F;
          }

          int distToFront = maxDist - d;
          if (distToFront < 6) {
            intensity *= distToFront / 6.0F;
          }

          intensity = Math.max(0.0F, Math.min(2.5F, intensity));
          float alphaCoreVal = 0.5F + intensity * 0.3F;
          if (isOverloading) {
            alphaCoreVal = 0.6F + intensity * 0.4F;
          }

          int alphaCore = Math.max(0, Math.min(255, (int)(alphaCoreVal * 255.0F)));
          int colCore = alphaCore << 24 | r << 16 | g << 8 | b;
          int wCore = intensity > 1.1F ? path.width : Math.max(1, path.width - 1);
          graphics.fill(px - wCore / 2, py - wCore / 2, px - wCore / 2 + wCore, py - wCore / 2 + wCore, colCore);
          float whiteIntensity = isOverloading ? intensity - 0.7F : intensity - 0.9F;
          if (whiteIntensity > 0.0F) {
            float alphaWhiteVal = whiteIntensity * 1.5F;
            int alphaWhite = Math.max(0, Math.min(255, (int)(alphaWhiteVal * 255.0F)));
            int colWhite = alphaWhite << 24 | 16777215;
            graphics.fill(px, py, px + 1, py + 1, colWhite);
          }
        }

        accumulatedDist += s.length;
      }

      if (maxDist > 0) {
        int[] point = new int[2];
        getPointOnPath(path, maxDist, point);
        int pxx = x + point[0];
        int pyx = y + point[1];
        int frontColor = -1275068416 | r << 16 | g << 8 | b;
        int rFront = path.width + 1;
        graphics.fill(pxx - rFront / 2, pyx - rFront / 2, pxx - rFront / 2 + rFront, pyx - rFront / 2 + rFront, frontColor);
      }
    }
  }

  public static void getPointOnPath(PlasmaWireRenderer.WirePath path, int dist, int[] outPoint) {
    int accumulated = 0;

    for (PlasmaWireRenderer.WireSegment s : path.segments) {
      if (dist <= accumulated + s.length) {
        int segmentDist = dist - accumulated;
        if (s.isVertical) {
          outPoint[0] = s.x1;
          outPoint[1] = s.y1 + (s.y2 >= s.y1 ? segmentDist : -segmentDist);
        } else {
          outPoint[0] = s.x1 + (s.x2 >= s.x1 ? segmentDist : -segmentDist);
          outPoint[1] = s.y1;
        }

        return;
      }

      accumulated += s.length;
    }

    if (!path.segments.isEmpty()) {
      PlasmaWireRenderer.WireSegment last = path.segments.get(path.segments.size() - 1);
      outPoint[0] = last.x2;
      outPoint[1] = last.y2;
    }
  }

  private PlasmaWireRenderer() {
  }

  public static class Spark {
    public float x;
    public float y;
    public float vx;
    public float vy;
    public int age;
    public int maxAge;
    public float scale;

    public Spark() {
    }
  }

  public static class WirePath {
    public final int width;
    public final List<PlasmaWireRenderer.WireSegment> segments;
    public final int totalLength;

    public WirePath(int width, List<PlasmaWireRenderer.WireSegment> segments) {
      this.width = width;
      this.segments = segments;
      int len = 0;

      for (PlasmaWireRenderer.WireSegment s : segments) {
        len += s.length;
      }

      this.totalLength = len;
    }
  }

  public static class WireSegment {
    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;
    public final int length;
    public final boolean isVertical;

    public WireSegment(int x1, int y1, int x2, int y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      this.isVertical = x1 == x2;
      this.length = this.isVertical ? Math.abs(y2 - y1) : Math.abs(x2 - x1);
    }
  }
}
