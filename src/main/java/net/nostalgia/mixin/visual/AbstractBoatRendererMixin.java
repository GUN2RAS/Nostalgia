package net.nostalgia.mixin.visual;

import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.nostalgia.duck.LegacyBoatRenderStateDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatRendererMixin {

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void nostalgia$setLegacyState(AbstractBoat entity, BoatRenderState state, float partialTick, CallbackInfo ci) {
        if ((entity.level() != null && net.nostalgia.world.rules.NostalgiaRules.getForLevel(entity.level()).fragileBoats) || entity instanceof net.nostalgia.entity.AlphaBoatEntity) {
            ((LegacyBoatRenderStateDuck) state).nostalgia$setLegacy(true);
        } else {
            ((LegacyBoatRenderStateDuck) state).nostalgia$setLegacy(false);
        }
    }
}
