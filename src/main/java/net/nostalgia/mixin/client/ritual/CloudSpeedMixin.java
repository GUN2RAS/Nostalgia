package net.nostalgia.mixin.client.ritual;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.InputStream;
import java.util.Optional;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public class CloudSpeedMixin {

    @Shadow
    private CloudRenderer.TextureData texture;

    @Shadow
    private boolean needsRebuild;

    @Unique
    private static final Identifier NOSTALGIA$ALPHA_CLOUDS_LOCATION = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/alpha_clouds.png");

    @Unique
    private CloudRenderer.TextureData nostalgia$vanillaTexture;

    @Unique
    private CloudRenderer.TextureData nostalgia$alphaTexture;

    @Unique
    private boolean nostalgia$wasInAlpha = false;

    @Inject(method = "apply(Ljava/util/Optional;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    private void nostalgia$onResourceReload(Optional<CloudRenderer.TextureData> preparations, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        this.nostalgia$vanillaTexture = this.texture;
        this.nostalgia$alphaTexture = null;
        this.nostalgia$wasInAlpha = false;
    }

    @Inject(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD")
    )
    private void nostalgia$swapCloudTextureForDimension(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        boolean isInAlpha = mc.level != null && mc.level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY;

        if (isInAlpha != this.nostalgia$wasInAlpha) {
            this.nostalgia$wasInAlpha = isInAlpha;
            if (isInAlpha) {
                if (this.nostalgia$vanillaTexture == null) {
                    this.nostalgia$vanillaTexture = this.texture;
                }
                if (this.nostalgia$alphaTexture == null) {
                    this.nostalgia$alphaTexture = nostalgia$loadAlphaTextureData(mc);
                }
                if (this.nostalgia$alphaTexture != null) {
                    this.texture = this.nostalgia$alphaTexture;
                    this.needsRebuild = true;
                }
            } else {
                if (this.nostalgia$vanillaTexture != null) {
                    this.texture = this.nostalgia$vanillaTexture;
                    this.needsRebuild = true;
                }
            }
        }
    }

    @Unique
    private static CloudRenderer.TextureData nostalgia$loadAlphaTextureData(Minecraft mc) {
        try (InputStream input = mc.getResourceManager().open(NOSTALGIA$ALPHA_CLOUDS_LOCATION);
             NativeImage img = NativeImage.read(input)) {
            int width = img.getWidth();
            int height = img.getHeight();
            long[] cells = new long[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = img.getPixel(x, y);
                    if (ARGB.alpha(pixel) < 10) {
                        cells[x + y * width] = 0L;
                    } else {
                        boolean north = ARGB.alpha(img.getPixel(x, Math.floorMod(y - 1, height))) < 10;
                        boolean east = ARGB.alpha(img.getPixel(Math.floorMod(x + 1, width), y)) < 10;
                        boolean south = ARGB.alpha(img.getPixel(x, Math.floorMod(y + 1, height))) < 10;
                        boolean west = ARGB.alpha(img.getPixel(Math.floorMod(x - 1, width), y)) < 10;
                        cells[x + y * width] = (long) pixel << 4 | (north ? 1L : 0L) << 3 | (east ? 1L : 0L) << 2 | (south ? 1L : 0L) << 1 | (west ? 1L : 0L);
                    }
                }
            }
            return new CloudRenderer.TextureData(cells, width, height);
        } catch (Exception e) {
            return null;
        }
    }

    @ModifyVariable(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private int nostalgia$fadeCloudAlpha(int color) {
        float alphaMultiplier = 1.0f;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            alphaMultiplier *= net.nostalgia.alphalogic.core.AlphaRenderState.getCelestialAlpha();
        }

        if (alphaMultiplier >= 1.0f) return color;

        int originalAlpha = (color >> 24) & 0xFF;
        int newAlpha = Math.max((int)(originalAlpha * alphaMultiplier), 0);

        return (color & 0x00FFFFFF) | (newAlpha << 24);
    }

    @Inject(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nostalgia$cancelFullyFadedClouds(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            if (net.nostalgia.alphalogic.core.AlphaRenderState.getCelestialAlpha() <= 0.01f) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private long nostalgia$accelerateCloudTime(long originalTime) {
        net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
        if (t == null || t.isBystander()) {
            long permanentOffset = (long) net.nostalgia.client.events.echo.RitualVisualManager.getDynamicCloudOffset(0, false);
            return originalTime - permanentOffset;
        }

        long offset = (long) net.nostalgia.client.events.echo.RitualVisualManager.getDynamicCloudOffset(originalTime, true);
        return originalTime - offset;
    }

    @ModifyVariable(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        ordinal = 1,
        argsOnly = true
    )
    private float nostalgia$accelerateCloudPartialTick(float originalPartialTick) {
        net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
        if (t == null || t.isBystander()) {
            double exactOffset = net.nostalgia.client.events.echo.RitualVisualManager.getDynamicCloudOffset(0, false);
            long intOffset = (long) exactOffset;
            return (float) (originalPartialTick - (exactOffset - intOffset));
        }

        double exactOffset = net.nostalgia.client.events.echo.RitualVisualManager.getDynamicCloudOffset(0, false);
        long intOffset = (long) exactOffset;
        double fracOffset = exactOffset - intOffset;

        return (float) (originalPartialTick - fracOffset);
    }
}
