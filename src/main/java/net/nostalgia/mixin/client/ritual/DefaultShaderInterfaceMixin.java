package net.nostalgia.mixin.client.ritual;

import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat3v;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DefaultShaderInterface.class, remap = false)
public class DefaultShaderInterfaceMixin {
    private GlUniformFloat3v uRitualCenter;
    private GlUniformFloat uRitualRadius;
    private GlUniformFloat uFogEnd;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
        this.uRitualCenter = context.bindUniformOptional("u_RitualCenter", GlUniformFloat3v::new);
        this.uRitualRadius = context.bindUniformOptional("u_RitualRadius", GlUniformFloat::new);
        this.uFogEnd = context.bindUniformOptional("u_FogEnd", GlUniformFloat::new);
    }

    @Inject(method = "setupState", at = @At("RETURN"))
    private void onSetupState(TerrainRenderPass pass, FogParameters parameters, com.mojang.blaze3d.textures.GpuSampler terrainSampler, CallbackInfo ci) {
        if (this.uRitualRadius == null || this.uRitualCenter == null) return;

        if (this.uFogEnd != null) {
            this.uFogEnd.setFloat(parameters.renderEnd());
        }

        net.minecraft.core.BlockPos c = null;
        float r = 0.0f;

        if (net.nostalgia.client.events.caches.UniversalHologramCache.debugOwer && net.nostalgia.client.events.caches.UniversalHologramCache.debugOwerCenter != null) {
            c = net.nostalgia.client.events.caches.UniversalHologramCache.debugOwerCenter;
            r = 320.0f;
        } else {
            net.nostalgia.alphalogic.ritual.event.SkyPortalEvent portal = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeSkyPortal();
            net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView t = net.nostalgia.client.events.core.ClientRitualEventRegistry.activeTransition();
            if (portal != null && portal.islandVisible()) {
                c = portal.center();
                r = (float) net.nostalgia.client.events.echo.RitualVisualManager.getAlphaRadius() + 16.0f;
            } else if (t != null) {
                c = t.ritualCenter();
                r = t.alphaRadius() + 16.0f;
            }
        }

        if (c != null && r > 0.0f) {
            net.minecraft.client.Camera camera = net.minecraft.client.Minecraft.getInstance().gameRenderer.getMainCamera();
            net.minecraft.world.phys.Vec3 camPos = camera.position();
            this.uRitualCenter.set((float)(c.getX() - camPos.x), (float)(c.getY() - camPos.y), (float)(c.getZ() - camPos.z));
            this.uRitualRadius.setFloat(r);
        } else {
            this.uRitualRadius.setFloat(0.0f);
        }
    }
}
