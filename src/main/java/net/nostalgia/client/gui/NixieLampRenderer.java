package net.nostalgia.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class NixieLampRenderer {

    public static void render(
            GuiGraphicsExtractor graphics,
            Font font,
            int lx, int ly,
            int w, int h,
            char ch,
            float brightness,
            int energyColor,
            boolean isHovered
    ) {
        int baseCol1 = isHovered ? TimeMachineLayout.RIVET_BRIGHT : TimeMachineLayout.GOLD_PRIMARY;
        int baseCol2 = isHovered ? TimeMachineLayout.GOLD_SECONDARY : TimeMachineLayout.GOLD_SHADOW;

        graphics.fill(lx + 1, ly + h - 3, lx + w - 1, ly + h, baseCol2);
        graphics.fill(lx + 1, ly + h - 4, lx + w - 1, ly + h - 3, baseCol1);
        graphics.fill(lx, ly + h - 3, lx + 1, ly + h, 0x44000000);
        graphics.fill(lx + w - 1, ly + h - 3, lx + w, ly + h, 0x44000000);

        graphics.fill(lx + 1, ly + 2, lx + w - 1, ly + h - 4, 0x18304045);
        graphics.fill(lx + 2, ly + 1, lx + w - 2, ly + 2, 0x18304045);

        graphics.fill(lx + 1, ly + 1, lx + w - 1, ly + 2, 0x22112233);
        graphics.fill(lx, ly + 2, lx + 1, ly + h - 4, 0x22112233);
        graphics.fill(lx + w - 1, ly + 2, lx + w, ly + h - 4, 0x22112233);

        int gridColor = 0x0E000000;
        for (int gx = lx + 2; gx < lx + w - 2; gx += 2) {
            for (int gy = ly + 3; gy < ly + h - 5; gy += 2) {
                graphics.fill(gx, gy, gx + 1, gy + 1, gridColor);
            }
        }

        graphics.fill(lx + 2, ly + 3, lx + 3, ly + h - 6, 0x33FFFFFF);
        graphics.fill(lx + 3, ly + 2, lx + 4, ly + 4, 0x11FFFFFF);
        graphics.fill(lx + 3, ly + 2, lx + w - 3, ly + 3, 0x22FFFFFF);

        if (ch != ' ' && brightness > 0.0f) {
            String sym = String.valueOf(ch);
            int textW = font.width(sym);
            int tx = lx + (w - textW) / 2 + 1;
            int ty = ly + (h - 8) / 2 - 1;

            int energyRGB = energyColor & 0xFFFFFF;
            int glowAlphaOuter = (int) (110 * brightness);
            int glowAlphaInner = (int) (190 * brightness);

            graphics.text(font, sym, tx - 1, ty, (glowAlphaOuter << 24) | energyRGB, false);
            graphics.text(font, sym, tx + 1, ty, (glowAlphaOuter << 24) | energyRGB, false);
            graphics.text(font, sym, tx, ty - 1, (glowAlphaOuter << 24) | energyRGB, false);
            graphics.text(font, sym, tx, ty + 1, (glowAlphaOuter << 24) | energyRGB, false);

            int glowColInner = blendColor(0xFFFF7700, energyColor, 0.4f);
            graphics.text(font, sym, tx, ty, (glowAlphaInner << 24) | (glowColInner & 0xFFFFFF), false);

            int coreAlpha = (int) (255 * brightness);
            int coreColor = 0xFFFFF0A0;
            graphics.text(font, sym, tx, ty, (coreAlpha << 24) | (coreColor & 0xFFFFFF), false);
        }
    }

    public static int blendColor(int color1, int color2, float ratio) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int) (a1 + (a2 - a1) * ratio);
        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private NixieLampRenderer() {}
}
