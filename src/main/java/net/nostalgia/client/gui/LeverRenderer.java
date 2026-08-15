package net.nostalgia.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class LeverRenderer {
  public static void render(
    GuiGraphicsExtractor graphics,
    Font font,
    int x,
    int y,
    float gameTime,
    float renderedProgress,
    float partialTick,
    boolean isOverloading,
    int overloadFrames,
    int currentEnergyColor
  ) {
    int btnGoX = x + 125;
    int btnGoY = y + 118;
    graphics.fill(btnGoX, btnGoY, btnGoX + 18, btnGoY + 24, -11912192);
    graphics.fill(btnGoX + 1, btnGoY + 1, btnGoX + 18 - 1, btnGoY + 24 - 1, -3825624);
    graphics.fill(btnGoX + 2, btnGoY + 2, btnGoX + 18 - 2, btnGoY + 24 - 2, -14805488);
    graphics.fill(btnGoX + 3, btnGoY + 3, btnGoX + 4, btnGoY + 4, -8754608);
    graphics.fill(btnGoX + 14, btnGoY + 3, btnGoX + 15, btnGoY + 4, -8754608);
    graphics.fill(btnGoX + 3, btnGoY + 20, btnGoX + 4, btnGoY + 21, -8754608);
    graphics.fill(btnGoX + 14, btnGoY + 20, btnGoX + 15, btnGoY + 21, -8754608);
    graphics.fill(btnGoX + 7, btnGoY + 5, btnGoX + 11, btnGoY + 19, -16777216);
    graphics.fill(btnGoX + 8, btnGoY + 6, btnGoX + 10, btnGoY + 18, -16118512);
    int cx = btnGoX + 9;
    int cy = btnGoY + 12;
    float t = isOverloading ? Math.min(1.0F, (overloadFrames + partialTick) / 4.0F) : 0.0F;
    float curDx = -3.0F + 6.0F * t;
    float curDy = 5.0F - 10.0F * t;
    int lx2 = cx + (int)curDx;
    int ly2 = cy + (int)curDy;
    graphics.fill(cx - 1, cy - 1, cx + 2, cy + 2, -11184811);
    int minLx = Math.min(cx, lx2);
    int maxLx = Math.max(cx, lx2);
    int minLy = Math.min(cy, ly2);
    int maxLy = Math.max(cy, ly2);
    graphics.fill(minLx, minLy, maxLx + 1, maxLy + 1, -7829368);
    graphics.fill(minLx + 1, minLy, maxLx + 1, maxLy + 1, -4473925);
    int headX = lx2 - 2;
    int headY = ly2 - 2;
    int headColor;
    if (isOverloading) {
      headColor = currentEnergyColor;
    } else if (renderedProgress >= 1.0F) {
      headColor = -2838729;
    } else {
      headColor = -14017008;
    }

    graphics.fill(headX, headY, headX + 5, headY + 5, -12770540);
    graphics.fill(headX + 1, headY + 1, headX + 4, headY + 4, headColor);
    graphics.fill(headX + 2, headY + 1, headX + 3, headY + 2, -1);
  }

  private LeverRenderer() {
  }
}
