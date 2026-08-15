package net.nostalgia.mixin.rd132211;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public LocalPlayer player;
    @Shadow
    public HitResult hitResult;
    @Shadow
    protected int rightClickDelay;
    @Shadow
    public MultiPlayerGameMode gameMode;

    @Shadow
    protected abstract void startUseItem();

    @Unique
    private boolean nostalgia$isRedirecting = false;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null && this.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {

            nostalgia$isRedirecting = true;
            this.startUseItem();
            nostalgia$isRedirecting = false;
            
            this.rightClickDelay = 10;
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void onContinueAttack(boolean $$0, CallbackInfo ci) {
        if (this.player != null && this.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            
            ci.cancel();
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void onStartUseItem(CallbackInfo ci) {
        if (this.player != null && this.player.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            if (nostalgia$isRedirecting) {
                
                return;
            }

            if (this.hitResult instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
                this.gameMode.startDestroyBlock(bhr.getBlockPos(), bhr.getDirection());
                this.player.swing(InteractionHand.MAIN_HAND);
            }
            
            this.rightClickDelay = 10;
            ci.cancel();
        }
    }
}
