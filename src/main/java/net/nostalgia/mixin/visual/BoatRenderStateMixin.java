package net.nostalgia.mixin.visual;

import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.nostalgia.duck.LegacyBoatRenderStateDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatRenderState.class)
public class BoatRenderStateMixin implements LegacyBoatRenderStateDuck {
    
    @Unique
    private boolean nostalgia$legacy = false;

    @Override
    public boolean nostalgia$isLegacy() {
        return this.nostalgia$legacy;
    }

    @Override
    public void nostalgia$setLegacy(boolean legacy) {
        this.nostalgia$legacy = legacy;
    }
}
