package net.nostalgia.client.ritual;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

public class ScreenFreezer {
    private static RenderTarget frozenScreen;
    public static boolean isFrozen = false;
    private static long freezeStartTime = 0;
    
    public static void takeSnapshot() {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();
        
        if (frozenScreen == null || frozenScreen.width != main.width || frozenScreen.height != main.height) {
            if (frozenScreen != null) frozenScreen.destroyBuffers();
            frozenScreen = new TextureTarget("Frozen Screen", main.width, main.height, false);
        }
        
        RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(
            main.getColorTexture(), 
            frozenScreen.getColorTexture(), 
            0, 0, 0, 0, 0, main.width, main.height
        );
        
        isFrozen = true;
        freezeStartTime = System.currentTimeMillis();
    }
    
    public static void renderFrozenScreen() {
        if (!isFrozen || frozenScreen == null) return;

        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(
            frozenScreen.getColorTexture(), 
            main.getColorTexture(), 
            0, 0, 0, 0, 0, main.width, main.height
        );
    }

    public static void tick() {
        if (isFrozen) {
            Minecraft client = Minecraft.getInstance();

            if (RitualVisualManager.isTransitioning) {
                if (RitualVisualManager.isInNewDimension() && !RitualVisualManager.waitingForChunks) {
                    isFrozen = false;
                    if (frozenScreen != null) {
                        frozenScreen.destroyBuffers();
                        frozenScreen = null;
                    }
                }
                return;
            }

            if (System.currentTimeMillis() - freezeStartTime > 100) {

                if (!(client.screen instanceof net.minecraft.client.gui.screens.LevelLoadingScreen)) {
                    isFrozen = false;
                    RitualVisualManager.endTransition();
                    if (frozenScreen != null) {
                        frozenScreen.destroyBuffers();
                        frozenScreen = null;
                    }
                }
            }
        }
    }
}
