package net.nostalgia.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.nostalgia.client.NostalgiaConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private long fadeOutStart;

    @Shadow
    private long fadeInStart;

    @Shadow
    @Final
    private boolean fadeIn;

    @Shadow
    private float currentProgress;

    @Shadow
    protected abstract void extractProgressBar(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, float fade);

    private static final Identifier LEGACY_LOCATION = Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/title/mojang_legacy.png");
    private static final Identifier BETA_LOCATION = Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/title/mojang_beta.png");
    private static final Identifier SPECS_LOCATION = Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/title/mojang_specifications.png");

    private static long splashStartTime = -1L;

    @Inject(method = "registerTextures", at = @At("TAIL"))
    private static void onRegisterTextures(net.minecraft.client.renderer.texture.TextureManager textureManager, CallbackInfo ci) {
        try {
            
            textureManager.registerAndLoad(LEGACY_LOCATION, new net.minecraft.client.renderer.texture.ReloadableTexture(LEGACY_LOCATION) {
                @Override
                public net.minecraft.client.renderer.texture.TextureContents loadContents(net.minecraft.server.packs.resources.ResourceManager resourceManager) throws java.io.IOException {
                    try (java.io.InputStream is = java.nio.file.Files.newInputStream(net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("nostalgia").get().findPath("assets/nostalgia/textures/gui/title/mojang_legacy.png").orElseThrow())) {
                         return new net.minecraft.client.renderer.texture.TextureContents(com.mojang.blaze3d.platform.NativeImage.read(is), new net.minecraft.client.resources.metadata.texture.TextureMetadataSection(true, true, net.minecraft.client.renderer.texture.MipmapStrategy.MEAN, 0.0F));
                    } catch (Exception e) { return null; }
                }
            });
            
            textureManager.registerAndLoad(BETA_LOCATION, new net.minecraft.client.renderer.texture.ReloadableTexture(BETA_LOCATION) {
                @Override
                public net.minecraft.client.renderer.texture.TextureContents loadContents(net.minecraft.server.packs.resources.ResourceManager resourceManager) throws java.io.IOException {
                    try (java.io.InputStream is = java.nio.file.Files.newInputStream(net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("nostalgia").get().findPath("assets/nostalgia/textures/gui/title/mojang_beta.png").orElseThrow())) {
                         return new net.minecraft.client.renderer.texture.TextureContents(com.mojang.blaze3d.platform.NativeImage.read(is), new net.minecraft.client.resources.metadata.texture.TextureMetadataSection(true, true, net.minecraft.client.renderer.texture.MipmapStrategy.MEAN, 0.0F));
                    } catch (Exception e) { return null; }
                }
            });
            
            textureManager.registerAndLoad(SPECS_LOCATION, new net.minecraft.client.renderer.texture.ReloadableTexture(SPECS_LOCATION) {
                @Override
                public net.minecraft.client.renderer.texture.TextureContents loadContents(net.minecraft.server.packs.resources.ResourceManager resourceManager) throws java.io.IOException {
                    try (java.io.InputStream is = java.nio.file.Files.newInputStream(net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("nostalgia").get().findPath("assets/nostalgia/textures/gui/title/mojang_specifications.png").orElseThrow())) {
                         return new net.minecraft.client.renderer.texture.TextureContents(com.mojang.blaze3d.platform.NativeImage.read(is), new net.minecraft.client.resources.metadata.texture.TextureMetadataSection(true, true, net.minecraft.client.renderer.texture.MipmapStrategy.MEAN, 0.0F));
                    } catch (Exception e) { return null; }
                }
            });
        } catch (Exception ignored) {}
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void onExtractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (!NostalgiaConfig.get().alphaLoadingScreen) {
            return;
        }

        ci.cancel();

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        long now = Util.getMillis();

        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = now;
        }

        if (splashStartTime == -1L) {
            splashStartTime = now;
        }

        boolean isFadingOut = this.fadeOutStart > -1L;
        float fadeOutAnim = isFadingOut ? (float)(now - this.fadeOutStart) / 1000.0F : -1.0F;
        float fadeInAnim = this.fadeInStart > -1L ? (float)(now - this.fadeInStart) / 500.0F : -1.0F;

        
        int state = (int) (splashStartTime % 3L); 

        
        boolean shouldDrawBackground = (this.minecraft.screen != null) && (this.fadeIn || isFadingOut);
        if (shouldDrawBackground) {
            this.minecraft.screen.extractRenderStateWithTooltipAndSubtitles(graphics, mouseX, mouseY, a);
        } else {
            this.minecraft.gui.extractDeferredSubtitles();
        }

        
        if (fadeOutAnim < 1.0F) {
            graphics.nextStratum();
            
            float progress = Mth.clamp(fadeOutAnim, 0.0F, 1.0F);

            int alphaBase = (this.fadeIn && fadeInAnim < 1.0F) ? Mth.ceil(Mth.clamp((double)fadeInAnim, 0.15, 1.0) * 255.0) : 255;
            int logoColor = ARGB.white(alphaBase);
            int bgColor = ARGB.color(alphaBase, 239, 50, 61);
            if (state == 1 || state == 2) bgColor = ARGB.color(alphaBase, 255, 255, 255);

            int centerX = width / 2;
            int centerY = height / 2;

            if (progress > 0.0F) {
                
                int blockSize = 32; 
                float maxRadius = (float) Math.sqrt(centerX * centerX + centerY * centerY);
                float currentRadius = maxRadius * progress * 1.5F; 

                for (int y = 0; y < height; y += blockSize) {
                    for (int x = 0; x < width; x += blockSize) {
                        float dx = (x + blockSize / 2.0F) - centerX;
                        float dy = (y + blockSize / 2.0F) - centerY;
                        float distSq = dx * dx + dy * dy;

                        int cellX1 = Math.min(x + blockSize, width);
                        int cellY1 = Math.min(y + blockSize, height);

                        float dist = (float) Math.sqrt(distSq);

                        
                        float eatenAmount = currentRadius - dist;

                        if (eatenAmount > 0) {
                            
                            
                            if (eatenAmount <= 48.0F) {
                                graphics.fill(x, y, cellX1, cellY1, 0xFFFFFFFF);
                            }
                            
                            continue;
                        }

                        
                        graphics.fill(x, y, cellX1, cellY1, bgColor);

                        
                        if (state == 0) {
                            drawChoppedBlit(graphics, RenderPipelines.MOJANG_LOGO, LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION,
                                    x, y, cellX1, cellY1, centerX - 120, centerY - 30, 120, 60, -0.0625f, 0.0f, 120, 60, 120, 120, logoColor);
                            drawChoppedBlit(graphics, RenderPipelines.MOJANG_LOGO, LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION,
                                    x, y, cellX1, cellY1, centerX, centerY - 30, 120, 60, 0.0625f, 60.0f, 120, 60, 120, 120, logoColor);
                        } else if (state == 1) {
                            drawChoppedBlit(graphics, net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, LEGACY_LOCATION,
                                    x, y, cellX1, cellY1, centerX - 128, centerY - 128, 256, 256, 0.0f, 0.0f, 256, 256, 256, 256, logoColor);
                        } else if (state == 2) {
                            drawChoppedBlit(graphics, net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, BETA_LOCATION,
                                    x, y, cellX1, cellY1, centerX - 128, centerY - 128, 256, 256, 0.0f, 0.0f, 128, 128, 128, 128, logoColor);
                        }
                    }
                }
            } else {
                
                graphics.fill(0, 0, width, height, bgColor);
                
                if (state == 0) {
                    graphics.blit(RenderPipelines.MOJANG_LOGO, LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION, centerX - 120, centerY - 30, -0.0625F, 0.0F, 120, 60, 120, 60, 120, 120, logoColor);
                    graphics.blit(RenderPipelines.MOJANG_LOGO, LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION, centerX, centerY - 30, 0.0625F, 60.0F, 120, 60, 120, 60, 120, 120, logoColor);
                } else if (state == 1) {
                    graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, LEGACY_LOCATION, centerX - 128, centerY - 128, 0.0f, 0.0f, 256, 256, 256, 256, 256, 256, logoColor);
                } else if (state == 2) {
                    graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, BETA_LOCATION, centerX - 128, centerY - 128, 0.0f, 0.0f, 256, 256, 128, 128, 128, 128, logoColor);
                }

                if (state == 0 || state == 1) {
                    int barY = (int)(height * 0.8325);
                    this.extractProgressBar(graphics, width / 2 - 120, barY - 5, width / 2 + 120, barY + 5, 1.0F);
                }
            }
        }

        if (fadeOutAnim >= 1.0F) { 
            this.minecraft.setOverlay(null);
            splashStartTime = -1L;
        }
    }

    private void drawChoppedBlit(GuiGraphicsExtractor graphics, com.mojang.blaze3d.pipeline.RenderPipeline pipeline, Identifier location,
                                 int cellX0, int cellY0, int cellX1, int cellY1,
                                 int logoX, int logoY, int logoW, int logoH,
                                 float u0, float v0, float uW, float vH, int texW, int texH, int color) {
        int intersectX0 = Math.max(cellX0, logoX);
        int intersectY0 = Math.max(cellY0, logoY);
        int intersectX1 = Math.min(cellX1, logoX + logoW);
        int intersectY1 = Math.min(cellY1, logoY + logoH);

        if (intersectX1 > intersectX0 && intersectY1 > intersectY0) {
            float pctLeft = (float)(intersectX0 - logoX) / logoW;
            float pctRight = (float)(intersectX1 - logoX) / logoW;
            float pctTop = (float)(intersectY0 - logoY) / logoH;
            float pctBottom = (float)(intersectY1 - logoY) / logoH;

            float drawU0 = u0 + uW * pctLeft;
            float drawV0 = v0 + vH * pctTop;
            float drawUW = uW * (pctRight - pctLeft);
            float drawVH = vH * (pctBottom - pctTop);

            graphics.blit(pipeline, location, intersectX0, intersectY0, drawU0, drawV0, intersectX1 - intersectX0, intersectY1 - intersectY0, (int)drawUW, (int)drawVH, texW, texH, color);
        }
    }
}
