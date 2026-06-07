package net.nostalgia.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import java.util.Random;

public final class CrtMonitorRenderer {

    public static void render(
            GuiGraphicsExtractor graphics,
            int x, int y,
            float gameTime,
            boolean isOverloading,
            float overloadProgress,
            int currentEnergyColor,
            Identifier currentIcon
    ) {
        int vX = x + TimeMachineLayout.MONITOR_X;
        int vY = y + TimeMachineLayout.MONITOR_Y;

        graphics.fill(vX - 2, vY - 2, vX + TimeMachineLayout.MONITOR_W + 2, vY + TimeMachineLayout.MONITOR_H + 2, 0xFF000000);

        int shakeX = 0;
        int shakeY = 0;
        if (isOverloading) {
            shakeX = (int) ((Math.random() - 0.5) * overloadProgress * 6);
            shakeY = (int) ((Math.random() - 0.5) * overloadProgress * 6);
        }
        int ovX = vX + shakeX;
        int ovY = vY + shakeY;

        int borderL = vX - 2;
        int borderT = vY - 2;
        int borderR = vX + TimeMachineLayout.MONITOR_W + 2;
        int borderB = vY + TimeMachineLayout.MONITOR_H + 2;

        graphics.fill(borderL, borderT, borderR - 1, borderT + 1, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(borderR - 1, borderT, borderR, borderT + 1, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(borderL, borderT + 1, borderL + 1, borderT + 2, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(borderL + 1, borderT + 1, borderR - 1, borderT + 2, TimeMachineLayout.GOLD_PRIMARY);
        graphics.fill(borderR - 1, borderT + 1, borderR, borderT + 2, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(borderL, borderT + 2, borderL + 1, borderB - 2, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(borderL + 1, borderT + 2, borderL + 2, borderB - 2, TimeMachineLayout.GOLD_BORDER_L);
        graphics.fill(borderR - 2, borderT + 2, borderR - 1, borderB - 2, TimeMachineLayout.GOLD_BORDER_R);
        graphics.fill(borderR - 1, borderT + 2, borderR, borderB - 2, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(borderL, borderB - 2, borderL + 1, borderB - 1, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(borderL + 1, borderB - 2, borderR - 1, borderB - 1, TimeMachineLayout.GOLD_SECONDARY);
        graphics.fill(borderR - 1, borderB - 2, borderR, borderB - 1, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(borderL, borderB - 1, borderR, borderB, TimeMachineLayout.GOLD_SHADOW);

        graphics.enableScissor(vX, vY, vX + TimeMachineLayout.MONITOR_W, vY + TimeMachineLayout.MONITOR_H);
        float panX = (float) Math.sin(gameTime / 4000.0f) * 10.0f;

        graphics.blit(RenderPipelines.GUI_TEXTURED, currentIcon, (int) (ovX - 10 + panX), ovY - 12, 0.0F, 0.0F, 180, 90, 180, 90);

        int scrollY = isOverloading ? (int) ((gameTime / 5.0f) % 4) : (int) ((gameTime / 20.0f) % 4);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TimeMachineLayout.CRT_SCANLINES, ovX, ovY, 0.0F, scrollY, TimeMachineLayout.MONITOR_W, TimeMachineLayout.MONITOR_H, 4, 4);

        if (isOverloading) {
            graphics.fill(vX, vY, vX + TimeMachineLayout.MONITOR_W, vY + TimeMachineLayout.MONITOR_H, (0x44 << 24) | currentEnergyColor);
            if (overloadProgress > 0.3f) {
                Random glitchRand = new Random((long) (gameTime / 70.0f));
                int numGlitches = glitchRand.nextInt((int) (overloadProgress * 4)) + 1;
                for (int i = 0; i < numGlitches; i++) {
                    int gh = glitchRand.nextInt(6) + 2;
                    int gy = ovY + glitchRand.nextInt(TimeMachineLayout.MONITOR_H - gh);
                    int gx = ovX + glitchRand.nextInt(15) - 7;
                    int col = (glitchRand.nextInt(80) + 40) << 24 | (currentEnergyColor & 0xFFFFFF);
                    graphics.fill(ovX, gy, ovX + TimeMachineLayout.MONITOR_W, gy + gh, col);
                    if (glitchRand.nextFloat() < 0.5f) {
                        graphics.fill(gx, gy, gx + TimeMachineLayout.MONITOR_W, gy + gh, (60 << 24) | 0xFFFFFF);
                    }
                }
            }
        }
        graphics.disableScissor();
    }

    private CrtMonitorRenderer() {}
}
