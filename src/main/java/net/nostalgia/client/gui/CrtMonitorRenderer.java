package net.nostalgia.client.gui;

import java.util.Random;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class CrtMonitorRenderer {
  public static void render(
    GuiGraphicsExtractor graphics,
    Font font,
    int x,
    int y,
    float gameTime,
    boolean isOverloading,
    float overloadProgress,
    int currentEnergyColor,
    Identifier currentIcon,
    float terminalAlpha,
    long timeSinceOpen,
    float noSignalAlpha,
    float signalAcquiredAlpha,
    float bootTextAlpha,
    float glitchAlpha
  ) {
    int vX = x + 8;
    int vY = y + 14;
    int shakeX = 0;
    int shakeY = 0;
    if (isOverloading) {
      shakeX = (int)((Math.random() - 0.5) * overloadProgress * 6.0);
      shakeY = (int)((Math.random() - 0.5) * overloadProgress * 6.0);
    }

    int ovX = vX + shakeX;
    int ovY = vY + shakeY;
    int bgAlpha = (int)(255.0F * terminalAlpha);
    graphics.fill(vX, vY, vX + 160, vY + 62, bgAlpha << 24 | 328192);
    graphics.enableScissor(vX, vY, vX + 160, vY + 62);
    if (noSignalAlpha > 0.0F) {
      String text = "NO SIGNAL";
      float scale = 1.2F;
      float scaledW = font.width(text) * scale;
      float scaledH = 8.0F * scale;
      float localX = (160.0F - scaledW) / 2.0F;
      float localY = (62.0F - scaledH) / 2.0F;
      int textAlpha = (int)(255.0F * terminalAlpha * noSignalAlpha);
      int tMainCol = textAlpha << 24 | 16724736;
      int tShadowCol = textAlpha << 24 | 5244928;
      graphics.pose().pushMatrix();
      graphics.pose().translate(ovX + localX, ovY + localY);
      graphics.pose().scale(scale, scale);
      graphics.text(font, text, 1, 1, tShadowCol, false);
      graphics.text(font, text, 0, 0, tMainCol, false);
      graphics.pose().popMatrix();
    }

    if (glitchAlpha > 0.0F) {
      Random rand = new Random((long)(gameTime * 13.0F));
      int numGlitches = rand.nextInt(7) + 5;

      for (int i = 0; i < numGlitches; i++) {
        int gy = ovY + rand.nextInt(62);
        int gh = rand.nextInt(4) + 1;
        int glitchA = (int)((rand.nextInt(180) + 70) * terminalAlpha * glitchAlpha);
        int col = glitchA << 24 | 16733440;
        graphics.fill(ovX, gy, ovX + 160, gy + gh, col);
        if (rand.nextFloat() < 0.2F) {
          int overlayAlpha = (int)(170.0F * terminalAlpha * glitchAlpha);
          graphics.fill(ovX + rand.nextInt(140), gy, ovX + 160, gy + gh, overlayAlpha << 24 | 16777215);
        }
      }
    }

    if (signalAcquiredAlpha > 0.0F) {
      String text = "SIGNAL ACQUIRED";
      float scale = 1.2F;
      float scaledW = font.width(text) * scale;
      float scaledH = 8.0F * scale;
      float localX = (160.0F - scaledW) / 2.0F;
      float localY = (62.0F - scaledH) / 2.0F;
      int textAlpha = (int)(255.0F * terminalAlpha * signalAcquiredAlpha);
      int tMainCol = textAlpha << 24 | 3407667;
      int tShadowCol = textAlpha << 24 | 17408;
      graphics.pose().pushMatrix();
      graphics.pose().translate(ovX + localX, ovY + localY);
      graphics.pose().scale(scale, scale);
      graphics.text(font, text, 1, 1, tShadowCol, false);
      graphics.text(font, text, 0, 0, tMainCol, false);
      graphics.pose().popMatrix();
    }

    if (bootTextAlpha > 0.0F) {
      int orangeAlpha = (int)(255.0F * terminalAlpha * bootTextAlpha);
      int mainCol = orangeAlpha << 24 | 16758835;
      int shadowCol = orangeAlpha << 24 | 8400896;
      int ty = ovY + 6;
      boolean drawCursor = timeSinceOpen / 150L % 2L == 0L;
      if (timeSinceOpen < 150L) {
        if (drawCursor) {
          graphics.text(font, "_", ovX + 6, ty, mainCol, false);
        }
      } else if (timeSinceOpen < 350L) {
        graphics.text(font, "> BOOTING SYSTEM...", ovX + 6, ty, mainCol, false);
        if (drawCursor) {
          graphics.text(font, "_", ovX + 6, ty + 10, mainCol, false);
        }
      } else if (timeSinceOpen < 550L) {
        graphics.text(font, "> BOOTING SYSTEM...", ovX + 6, ty, mainCol, false);
        graphics.text(font, "> INIT MEMORY CACHE...", ovX + 6, ty + 10, mainCol, false);
        if (drawCursor) {
          graphics.text(font, "_", ovX + 6, ty + 20, mainCol, false);
        }
      } else if (timeSinceOpen < 750L) {
        graphics.text(font, "> BOOTING SYSTEM...", ovX + 6, ty, mainCol, false);
        graphics.text(font, "> INIT MEMORY CACHE...", ovX + 6, ty + 10, mainCol, false);
        graphics.text(font, "> LINK ESTABLISHED.", ovX + 6, ty + 20, mainCol, false);
        if (drawCursor) {
          graphics.text(font, "_", ovX + 6, ty + 30, mainCol, false);
        }
      } else if (timeSinceOpen < 950L) {
        graphics.text(font, "> BOOTING SYSTEM...", ovX + 6, ty, mainCol, false);
        graphics.text(font, "> INIT MEMORY CACHE...", ovX + 6, ty + 10, mainCol, false);
        graphics.text(font, "> LINK ESTABLISHED.", ovX + 6, ty + 20, mainCol, false);
        graphics.text(font, "> STATUS: READY", ovX + 6, ty + 30, mainCol, false);
        if (drawCursor) {
          graphics.text(font, "_", ovX + 6, ty + 40, mainCol, false);
        }
      } else {
        String text = "ALPHA PROTOCOL";
        float scale = Math.min(1.8F, 150.0F / font.width(text));
        float scaledW = font.width(text) * scale;
        float scaledH = 8.0F * scale;
        float localX = (160.0F - scaledW) / 2.0F;
        float localY = (62.0F - scaledH) / 2.0F;
        float textFade = Math.min(1.0F, ((float)timeSinceOpen - 950.0F) / 250.0F);
        int textAlphaVal = (int)(255.0F * terminalAlpha * bootTextAlpha * textFade);
        int tMainCol = textAlphaVal << 24 | 16758835;
        int tShadowCol = textAlphaVal << 24 | 8400896;
        graphics.pose().pushMatrix();
        graphics.pose().translate(ovX + localX, ovY + localY);
        graphics.pose().scale(scale, scale);
        graphics.text(font, text, 1, 1, tShadowCol, false);
        graphics.text(font, text, 0, 0, tMainCol, false);
        graphics.pose().popMatrix();
      }
    }

    graphics.disableScissor();
    renderCrtOverlay(graphics, x, y, gameTime, terminalAlpha);
  }

  public static void renderBorderOnly(GuiGraphicsExtractor graphics, int x, int y, boolean isOverloading, float overloadProgress) {
    int vX = x + 8;
    int vY = y + 14;
    graphics.fill(vX - 2, vY - 2, vX + 160 + 2, vY + 62 + 2, -16777216);
    int borderL = vX - 2;
    int borderT = vY - 2;
    int borderR = vX + 160 + 2;
    int borderB = vY + 62 + 2;
    graphics.fill(borderL, borderT, borderR - 1, borderT + 1, -1840);
    graphics.fill(borderR - 1, borderT, borderR, borderT + 1, -10793984);
    graphics.fill(borderL, borderT + 1, borderL + 1, borderT + 2, -1840);
    graphics.fill(borderL + 1, borderT + 1, borderR - 1, borderT + 2, -2838729);
    graphics.fill(borderR - 1, borderT + 1, borderR, borderT + 2, -10793984);
    graphics.fill(borderL, borderT + 2, borderL + 1, borderB - 2, -1840);
    graphics.fill(borderL + 1, borderT + 2, borderL + 2, borderB - 2, -4680154);
    graphics.fill(borderR - 2, borderT + 2, borderR - 1, borderB - 2, -6389475);
    graphics.fill(borderR - 1, borderT + 2, borderR, borderB - 2, -10793984);
    graphics.fill(borderL, borderB - 2, borderL + 1, borderB - 1, -1840);
    graphics.fill(borderL + 1, borderB - 2, borderR - 1, borderB - 1, -7637760);
    graphics.fill(borderR - 1, borderB - 2, borderR, borderB - 1, -10793984);
    graphics.fill(borderL, borderB - 1, borderR, borderB, -10793984);
  }

  public static void renderOverloadOverlay(GuiGraphicsExtractor graphics, int x, int y, float gameTime, float overloadProgress, int currentEnergyColor) {
    int vX = x + 8;
    int vY = y + 14;
    int shakeX = (int)((Math.random() - 0.5) * overloadProgress * 6.0);
    int shakeY = (int)((Math.random() - 0.5) * overloadProgress * 6.0);
    int ovX = vX + shakeX;
    int ovY = vY + shakeY;
    graphics.enableScissor(vX, vY, vX + 160, vY + 62);
    graphics.fill(vX, vY, vX + 160, vY + 62, 1140850688 | currentEnergyColor);
    if (overloadProgress > 0.3F) {
      Random glitchRand = new Random((long)(gameTime / 70.0F));
      int numGlitches = glitchRand.nextInt((int)(overloadProgress * 4.0F)) + 1;

      for (int i = 0; i < numGlitches; i++) {
        int gh = glitchRand.nextInt(6) + 2;
        int gy = ovY + glitchRand.nextInt(62 - gh);
        int gx = ovX + glitchRand.nextInt(15) - 7;
        int col = glitchRand.nextInt(80) + 40 << 24 | currentEnergyColor & 16777215;
        graphics.fill(ovX, gy, ovX + 160, gy + gh, col);
        if (glitchRand.nextFloat() < 0.5F) {
          graphics.fill(gx, gy, gx + 160, gy + gh, 1023410175);
        }
      }
    }

    graphics.disableScissor();
  }

  public static void renderCrtOverlay(GuiGraphicsExtractor graphics, int x, int y, float gameTime) {
    renderCrtOverlay(graphics, x, y, gameTime, 1.0F);
  }

  public static void renderCrtOverlay(GuiGraphicsExtractor graphics, int x, int y, float gameTime, float alpha) {
    if (!(alpha <= 0.0F)) {
      int vX = x + 8;
      int vY = y + 14;
      int mW = 160;
      int mH = 62;
      graphics.enableScissor(vX, vY, vX + mW, vY + mH);
      int scrollY = (int)(gameTime / 20.0F % 4.0F);
      int scanlineColor = (int)(255.0F * alpha) << 24 | 16777215;
      graphics.blit(RenderPipelines.GUI_TEXTURED, TimeMachineLayout.CRT_SCANLINES, vX, vY, 0.0F, scrollY, mW, mH, 4, 4, scanlineColor);
      float noiseY = gameTime * 0.06F % (mH + 40) - 20.0F;
      if (noiseY >= 0.0F && noiseY < mH) {
        int noiseAlpha = (int)(15.0F * alpha);
        graphics.fill(vX, (int)(vY + noiseY), vX + mW, (int)(vY + noiseY + 6.0F), noiseAlpha << 24 | 16777215);
      }

      int cornerAlpha = (int)(255.0F * alpha);
      int cornerColor = cornerAlpha << 24 | 0;
      graphics.fill(vX, vY, vX + 2, vY + 1, cornerColor);
      graphics.fill(vX, vY + 1, vX + 1, vY + 2, cornerColor);
      graphics.fill(vX + mW - 2, vY, vX + mW, vY + 1, cornerColor);
      graphics.fill(vX + mW - 1, vY + 1, vX + mW, vY + 2, cornerColor);
      graphics.fill(vX, vY + mH - 1, vX + 2, vY + mH, cornerColor);
      graphics.fill(vX, vY + mH - 2, vX + 1, vY + mH - 1, cornerColor);
      graphics.fill(vX + mW - 2, vY + mH - 1, vX + mW, vY + mH, cornerColor);
      graphics.fill(vX + mW - 1, vY + mH - 2, vX + mW, vY + mH - 1, cornerColor);
      int vColor1 = (int)(68.0F * alpha) << 24 | 0;
      int vColor2 = (int)(34.0F * alpha) << 24 | 0;
      int vColor3 = (int)(16.0F * alpha) << 24 | 0;
      graphics.outline(vX, vY, mW, mH, vColor1);
      graphics.outline(vX + 1, vY + 1, mW - 2, mH - 2, vColor2);
      graphics.outline(vX + 2, vY + 2, mW - 4, mH - 4, vColor3);
      int glareColor = (int)(21.0F * alpha) << 24 | 16777215;
      graphics.fill(vX + 4, vY + 3, vX + 50, vY + 4, glareColor);
      graphics.fill(vX + 3, vY + 4, vX + 4, vY + 20, glareColor);
      graphics.disableScissor();
    }
  }

  public static void renderGlitchOverlay(GuiGraphicsExtractor graphics, int monX, int monY, float gameTime, float glitchAlpha) {
    Random rand = new Random((long)(gameTime * 13.0F));
    int numGlitches = rand.nextInt(7) + 5;
    float intensity = glitchAlpha;
    for (int i = 0; i < numGlitches; i++) {
      int gy = monY + rand.nextInt(62);
      int gh = rand.nextInt(4) + 1;
      int glitchA = (int)((rand.nextInt(180) + 70) * intensity);
      int col = glitchA << 24 | 16733440;
      graphics.fill(monX, gy, monX + 160, gy + gh, col);
      if (rand.nextFloat() < 0.3F) {
        int overlayAlpha = (int)(170.0F * intensity);
        graphics.fill(monX + rand.nextInt(140), gy, monX + 160, gy + gh, overlayAlpha << 24 | 16777215);
      }
    }
  }

  private CrtMonitorRenderer() {
  }
}
