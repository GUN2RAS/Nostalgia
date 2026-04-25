package net.nostalgia.mixin.ritual.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.nostalgia.alphalogic.ritual.RitualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {

    @WrapOperation(
        method = { "extractRenderState", "tickRainParticles" },
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getRainLevel(F)F")
    )
    private float nostalgia$wrapRainLevel(Level level, float partialTicks, Operation<Float> original) {
        float globalRain = original.call(level, partialTicks);
        
        
        if (RitualManager.hasAnyRainingZone(level.dimension())) {
            return Math.max(globalRain, 1.0F);
        }
        
        return globalRain;
    }

    @WrapOperation(
        method = "extractRenderState",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation nostalgia$wrapPrecipitationRender(WeatherEffectRenderer instance, Level level, BlockPos pos, Operation<Biome.Precipitation> original) {
        Biome.Precipitation biomePrecip = original.call(instance, level, pos);
        
        float localRain = RitualManager.getLocalRainLevel(level.dimension(), pos);
        if (localRain >= 0.0F) {
            
            if (localRain == 0.0F) {
                return Biome.Precipitation.NONE; 
            } else {
                return biomePrecip; 
            }
        } else {
            
            float globalRain = level.getRainLevel(1.0F);
            if (level instanceof net.nostalgia.mixin.client.ritual.LevelRainFieldAccessor acc) {
                globalRain = acc.nostalgia$getRainLevelField();
            }
            if (globalRain <= 0.01F) {
                return Biome.Precipitation.NONE; 
            } else {
                return biomePrecip;
            }
        }
    }

    @WrapOperation(
        method = "tickRainParticles",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation nostalgia$wrapPrecipitationTick(WeatherEffectRenderer instance, Level level, BlockPos pos, Operation<Biome.Precipitation> original) {
        Biome.Precipitation biomePrecip = original.call(instance, level, pos);
        
        float localRain = RitualManager.getLocalRainLevel(level.dimension(), pos);
        if (localRain >= 0.0F) {
            
            return Biome.Precipitation.NONE;
        } else {
            
            float globalRain = level.getRainLevel(1.0F);
            if (level instanceof net.nostalgia.mixin.client.ritual.LevelRainFieldAccessor acc) {
                globalRain = acc.nostalgia$getRainLevelField();
            }
            if (globalRain <= 0.01F) {
                return Biome.Precipitation.NONE; 
            } else {
                return biomePrecip;
            }
        }
    }

    @org.spongepowered.asm.mixin.injection.ModifyArgs(
        method = "extractRenderState",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;createRainColumnInstance(Lnet/minecraft/util/RandomSource;IIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;")
    )
    private void nostalgia$modifyRainArgs(org.spongepowered.asm.mixin.injection.invoke.arg.Args args) {
        int x = args.get(2);
        int z = args.get(5);
        BlockPos pos = new BlockPos(x, 0, z);
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null) {
            RitualManager.ActiveZone zone = RitualManager.getZoneAt(mc.level.dimension(), pos);
            if (zone != null) {
                args.set(1, (int) zone.snapClockTicks());
                args.set(7, 0.0F); 
            }
        }
    }

    @org.spongepowered.asm.mixin.injection.ModifyArgs(
        method = "extractRenderState",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;createSnowColumnInstance(Lnet/minecraft/util/RandomSource;IIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;")
    )
    private void nostalgia$modifySnowArgs(org.spongepowered.asm.mixin.injection.invoke.arg.Args args) {
        int x = args.get(2);
        int z = args.get(5);
        BlockPos pos = new BlockPos(x, 0, z);
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level != null) {
            RitualManager.ActiveZone zone = RitualManager.getZoneAt(mc.level.dimension(), pos);
            if (zone != null) {
                args.set(1, (int) zone.snapClockTicks());
                args.set(7, 0.0F); 
            }
        }
    }
}
