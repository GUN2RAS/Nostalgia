package net.nostalgia.client.gui;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class NixieLampRenderer {
  private static Identifier bgLarge;
  private static Identifier bgLargeHover;
  private static Identifier bgSmall;
  private static Identifier bgSmallHover;

  private static void initTextures() {
    if (bgLarge == null) {
      bgLarge = registerTexture("nixie_large", 12, 22, false);
      bgLargeHover = registerTexture("nixie_large_hover", 12, 22, true);
      bgSmall = registerTexture("nixie_small", 10, 22, false);
      bgSmallHover = registerTexture("nixie_small_hover", 10, 22, true);
    }
  }

  private static Identifier registerTexture(String name, int w, int h, boolean isHovered) {
    NativeImage img = new NativeImage(w, h, false);

    for (int py = 0; py < h; py++) {
      for (int px = 0; px < w; px++) {
        img.setPixel(px, py, 0);
      }
    }

    int baseCol1 = isHovered ? -3936 : -2838729;
    int baseCol2 = isHovered ? -7637760 : -10793984;
    fillRectBlend(img, 1, h - 3, w - 2, 3, baseCol2);
    fillRectBlend(img, 1, h - 4, w - 2, 1, baseCol1);
    fillRectBlend(img, 0, h - 3, 1, 3, 1140850688);
    fillRectBlend(img, w - 1, h - 3, 1, 3, 1140850688);
    fillRectBlend(img, 1, 2, w - 2, h - 6, 405815365);
    fillRectBlend(img, 2, 1, w - 4, 1, 405815365);
    fillRectBlend(img, 1, 1, w - 2, 1, 571548211);
    fillRectBlend(img, 0, 2, 1, h - 6, 571548211);
    fillRectBlend(img, w - 1, 2, 1, h - 6, 571548211);
    int gridColor = 234881024;

    for (int gx = 2; gx < w - 2; gx += 2) {
      for (int gy = 3; gy < h - 5; gy += 2) {
        setPixelBlend(img, gx, gy, gridColor);
      }
    }

    fillRectBlend(img, 2, 3, 1, h - 9, 872415231);
    fillRectBlend(img, 3, 2, 1, 2, 301989887);
    fillRectBlend(img, 3, 2, w - 6, 1, 587202559);
    Identifier id = Identifier.parse("nostalgia:" + name + "_" + System.identityHashCode(img));
    DynamicTexture dynamicTex = new DynamicTexture(() -> name, img);
    Minecraft.getInstance().getTextureManager().register(id, dynamicTex);
    return id;
  }

  private static void setPixelBlend(NativeImage img, int x, int y, int color) {
    int bg = img.getPixel(x, y);
    img.setPixel(x, y, blend(bg, color));
  }

  private static void fillRectBlend(NativeImage img, int x, int y, int w, int h, int color) {
    for (int py = y; py < y + h; py++) {
      for (int px = x; px < x + w; px++) {
        setPixelBlend(img, px, py, color);
      }
    }
  }

  private static int blend(int bg, int fg) {
    int aFg = fg >> 24 & 0xFF;
    if (aFg == 0) {
      return bg;
    } else {
      int aBg = bg >> 24 & 0xFF;
      if (aBg == 0) {
        return fg;
      } else {
        int rBg = bg >> 16 & 0xFF;
        int gBg = bg >> 8 & 0xFF;
        int bBg = bg & 0xFF;
        int rFg = fg >> 16 & 0xFF;
        int gFg = fg >> 8 & 0xFF;
        int bFg = fg & 0xFF;
        int a = aFg + aBg * (255 - aFg) / 255;
        int r = (rFg * aFg + rBg * aBg * (255 - aFg) / 255) / a;
        int g = (gFg * aFg + gBg * aBg * (255 - aFg) / 255) / a;
        int b = (bFg * aFg + bBg * aBg * (255 - aFg) / 255) / a;
        return a << 24 | r << 16 | g << 8 | b;
      }
    }
  }

  public static void render(
    GuiGraphicsExtractor graphics, Font font, int lx, int ly, int w, int h, char ch, float brightness, int energyColor, boolean isHovered
  ) {
    initTextures();
    Identifier bg = w == 12 ? (isHovered ? bgLargeHover : bgLarge) : (isHovered ? bgSmallHover : bgSmall);
    graphics.blit(RenderPipelines.GUI_TEXTURED, bg, lx, ly, 0.0F, 0.0F, w, h, w, h);
    if (ch != ' ' && brightness > 0.0F) {
      String sym = String.valueOf(ch);
      int textW = font.width(sym);
      int tx = lx + (w - textW) / 2 + 1;
      int ty = ly + (h - 8) / 2 - 1;
      int energyRGB = energyColor & 16777215;
      int glowAlphaOuter = (int)(110.0F * brightness);
      int glowAlphaInner = (int)(190.0F * brightness);
      graphics.text(font, sym, tx - 1, ty, glowAlphaOuter << 24 | energyRGB, false);
      graphics.text(font, sym, tx + 1, ty, glowAlphaOuter << 24 | energyRGB, false);
      graphics.text(font, sym, tx, ty - 1, glowAlphaOuter << 24 | energyRGB, false);
      graphics.text(font, sym, tx, ty + 1, glowAlphaOuter << 24 | energyRGB, false);
      int glowColInner = blendColor(-35072, energyColor, 0.4F);
      graphics.text(font, sym, tx, ty, glowAlphaInner << 24 | glowColInner & 16777215, false);
      int coreAlpha = (int)(255.0F * brightness);
      int coreColor = -3936;
      graphics.text(font, sym, tx, ty, coreAlpha << 24 | coreColor & 16777215, false);
    }
  }

  public static int blendColor(int color1, int color2, float ratio) {
    int a1 = color1 >> 24 & 0xFF;
    int r1 = color1 >> 16 & 0xFF;
    int g1 = color1 >> 8 & 0xFF;
    int b1 = color1 & 0xFF;
    int a2 = color2 >> 24 & 0xFF;
    int r2 = color2 >> 16 & 0xFF;
    int g2 = color2 >> 8 & 0xFF;
    int b2 = color2 & 0xFF;
    int a = (int)(a1 + (a2 - a1) * ratio);
    int r = (int)(r1 + (r2 - r1) * ratio);
    int g = (int)(g1 + (g2 - g1) * ratio);
    int b = (int)(b1 + (b2 - b1) * ratio);
    return a << 24 | r << 16 | g << 8 | b;
  }

  private NixieLampRenderer() {
  }
}
