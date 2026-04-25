package net.nostalgia.mixin.rd132211;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.dimension.DimensionType;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderState.class)
public class SkyRenderStateMixin {

    private static final int RD132211_SKY_COLOR = ARGB.color(255, 127, 204, 255);

    @Shadow
    public int skyColor;
    @Shadow
    public boolean shouldRenderDarkDisc;
    @Shadow
    public DimensionType.Skybox skybox;

    @Inject(method = "reset", at = @At("TAIL"))
    private void afterReset(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            
            this.skyColor = RD132211_SKY_COLOR;
            
            this.skybox = DimensionType.Skybox.OVERWORLD;
            
            this.shouldRenderDarkDisc = false;
        }
    }
}
