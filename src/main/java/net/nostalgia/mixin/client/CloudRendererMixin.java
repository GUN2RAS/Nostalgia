package net.nostalgia.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.InputStream;
import java.io.IOException;
import java.util.Optional;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {

    @Shadow
    private CloudRenderer.TextureData texture;
    @Shadow
    private boolean needsRebuild;

    @Unique
    private CloudRenderer.TextureData nostalgia$defaultTexture;
    @Unique
    private CloudRenderer.TextureData nostalgia$classicTexture;
    @Unique
    private Boolean nostalgia$wasClassicMode;

    @Inject(method = "apply", at = @At("TAIL"))
    private void nostalgia$applyClassicCloudTexture(Optional<CloudRenderer.TextureData> preparations, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        this.nostalgia$defaultTexture = preparations.orElse(null);
        this.nostalgia$classicTexture = null;
        Identifier classicLoc = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/alpha_clouds.png");
        try (
            InputStream input = manager.open(classicLoc);
            NativeImage img = NativeImage.read(input);
        ) {
            int width = img.getWidth();
            int height = img.getHeight();
            long[] cells = new long[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int color = img.getPixel(x, y);
                    if (ARGB.alpha(color) < 10) {
                        cells[x + y * width] = 0L;
                    } else {
                        boolean north = ARGB.alpha(img.getPixel(x, Math.floorMod(y - 1, height))) < 10;
                        boolean east = ARGB.alpha(img.getPixel(Math.floorMod(x + 1, width), y)) < 10;
                        boolean south = ARGB.alpha(img.getPixel(x, Math.floorMod(y + 1, height))) < 10;
                        boolean west = ARGB.alpha(img.getPixel(Math.floorMod(x - 1, width), y)) < 10;
                        cells[x + y * width] = (long)color << 4 | (north ? 1 : 0) << 3 | (east ? 1 : 0) << 2 | (south ? 1 : 0) << 1 | (west ? 1 : 0) << 0;
                    }
                }
            }
            this.nostalgia$classicTexture = new CloudRenderer.TextureData(cells, width, height);
        } catch (IOException e) {
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void nostalgia$beforeRender(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            boolean isClassic = net.nostalgia.world.rules.LegacyProfiles.get(mc.level).classicClouds();
            if (this.nostalgia$wasClassicMode == null || this.nostalgia$wasClassicMode != isClassic) {
                this.nostalgia$wasClassicMode = isClassic;
                this.needsRebuild = true;
            }
            if (isClassic && this.nostalgia$classicTexture != null) {
                this.texture = this.nostalgia$classicTexture;
            } else {
                this.texture = this.nostalgia$defaultTexture;
            }
        }
    }
}
