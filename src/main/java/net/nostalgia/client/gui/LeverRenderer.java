package net.nostalgia.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class LeverRenderer {

    public static void render(
            GuiGraphicsExtractor graphics,
            Font font,
            int x, int y,
            float gameTime,
            float renderedProgress,
            float partialTick,
            boolean isOverloading,
            int overloadFrames,
            int currentEnergyColor
    ) {
        int btnGoX = x + TimeMachineLayout.TOGGLE_X;
        int btnGoY = y + TimeMachineLayout.TOGGLE_Y;


        graphics.fill(btnGoX, btnGoY, btnGoX + TimeMachineLayout.TOGGLE_W, btnGoY + TimeMachineLayout.TOGGLE_H, TimeMachineLayout.TEXT_ON_OFF_COLOR);
        graphics.fill(btnGoX + 1, btnGoY + 1, btnGoX + TimeMachineLayout.TOGGLE_W - 1, btnGoY + TimeMachineLayout.TOGGLE_H - 1, TimeMachineLayout.RIVET_LIGHT);
        graphics.fill(btnGoX + 2, btnGoY + 2, btnGoX + TimeMachineLayout.TOGGLE_W - 2, btnGoY + TimeMachineLayout.TOGGLE_H - 2, TimeMachineLayout.LEVER_BG);

        graphics.fill(btnGoX + 3, btnGoY + 3, btnGoX + 4, btnGoY + 4, TimeMachineLayout.LEVER_SCREW);
        graphics.fill(btnGoX + 14, btnGoY + 3, btnGoX + 15, btnGoY + 4, TimeMachineLayout.LEVER_SCREW);
        graphics.fill(btnGoX + 3, btnGoY + 20, btnGoX + 4, btnGoY + 21, TimeMachineLayout.LEVER_SCREW);
        graphics.fill(btnGoX + 14, btnGoY + 20, btnGoX + 15, btnGoY + 21, TimeMachineLayout.LEVER_SCREW);

        graphics.fill(btnGoX + 7, btnGoY + 5, btnGoX + 11, btnGoY + 19, 0xFF000000);
        graphics.fill(btnGoX + 8, btnGoY + 6, btnGoX + 10, btnGoY + 18, TimeMachineLayout.LEVER_SLOT);

        int cx = btnGoX + 9;
        int cy = btnGoY + 12;
        float t = isOverloading ? Math.min(1.0f, (overloadFrames + partialTick) / 4.0f) : 0.0f;

        float curDx = -3.0f + 6.0f * t;
        float curDy = 5.0f - 10.0f * t;

        int lx2 = cx + (int) curDx;
        int ly2 = cy + (int) curDy;

        graphics.fill(cx - 1, cy - 1, cx + 2, cy + 2, 0xFF555555);
        int minLx = Math.min(cx, lx2);
        int maxLx = Math.max(cx, lx2);
        int minLy = Math.min(cy, ly2);
        int maxLy = Math.max(cy, ly2);
        graphics.fill(minLx, minLy, maxLx + 1, maxLy + 1, 0xFF888888);
        graphics.fill(minLx + 1, minLy, maxLx + 1, maxLy + 1, 0xFFBBBBBB);

        int headX = lx2 - 2;
        int headY = ly2 - 2;
        int headColor;
        if (isOverloading) {
            headColor = currentEnergyColor;
        } else if (renderedProgress >= 1.0f) {
            headColor = TimeMachineLayout.GOLD_PRIMARY;
        } else {
            headColor = 0xFF2A1E10;
        }

        graphics.fill(headX, headY, headX + 5, headY + 5, TimeMachineLayout.BACKGROUND_COLOR);
        graphics.fill(headX + 1, headY + 1, headX + 4, headY + 4, headColor);
        graphics.fill(headX + 2, headY + 1, headX + 3, headY + 2, 0xFFFFFFFF);
    }

    private LeverRenderer() {}
}
