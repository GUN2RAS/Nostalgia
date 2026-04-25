package net.nostalgia.mixin.alpha;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.core.AlphaPlayer;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class AlphaPlayerBridgeMixin {

    @org.spongepowered.asm.mixin.Shadow
    protected abstract void playStepSound(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state);

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void hijackAlphaTravel(Vec3 movementInput, CallbackInfo ci) {
        Player mcPlayer = (Player) (Object) this;

        if (mcPlayer.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY && !mcPlayer.isSpectator()) {
            
            if (mcPlayer.level().isClientSide() && mcPlayer instanceof LocalPlayer localPlayer) {
                AlphaPlayer alphaP = AlphaEngineManager.getAlphaPlayer(localPlayer);
                
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                alphaP.moveStrafing = 0.0f;
                alphaP.moveForward = 0.0f;
                if (mc.options.keyUp.isDown()) alphaP.moveForward += 1.0f;
                if (mc.options.keyDown.isDown()) alphaP.moveForward -= 1.0f;
                if (mc.options.keyLeft.isDown()) alphaP.moveStrafing += 1.0f;
                if (mc.options.keyRight.isDown()) alphaP.moveStrafing -= 1.0f;
                
                alphaP.isSneaking = mc.options.keyShift.isDown();
                if (alphaP.isSneaking) {
                    alphaP.moveStrafing *= 0.3f;
                    alphaP.moveForward *= 0.3f;
                }
                
                alphaP.isJumping = mc.options.keyJump.isDown(); 
                alphaP.inWater = localPlayer.isInWater();
                
                alphaP.rotationYaw = localPlayer.getYRot();
                alphaP.rotationPitch = localPlayer.getXRot();

                alphaP.onUpdate();

                if (localPlayer.isPassenger()) {
                    alphaP.setPosition(localPlayer.getX(), localPlayer.getY() + alphaP.yOffset - alphaP.ySize, localPlayer.getZ());
                    alphaP.fallDistance = 0;
                } else {
                    localPlayer.setPos(alphaP.posX, alphaP.boundingBox.minY, alphaP.posZ);
                }

                if (alphaP.distanceWalkedModified > alphaP.nextStepDistance && alphaP.onGround) {
                    alphaP.nextStepDistance = (int)alphaP.distanceWalkedModified + 1;
                    net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(
                        net.minecraft.util.Mth.floor(alphaP.posX),
                        net.minecraft.util.Mth.floor(alphaP.boundingBox.minY - 0.20000000298023224),
                        net.minecraft.util.Mth.floor(alphaP.posZ)
                    );
                    net.minecraft.world.level.block.state.BlockState state = localPlayer.level().getBlockState(pos);
                    this.playStepSound(pos, state);
                }

                localPlayer.setDeltaMovement(alphaP.motionX, alphaP.motionY, alphaP.motionZ);

                localPlayer.setSprinting(false);
                localPlayer.yya = 0;
                
                if (!localPlayer.isPassenger()) {
                    localPlayer.setOnGround(alphaP.onGround);
                }

                if (alphaP.fallDistance > 3.0F && alphaP.onGround) {
                }
            }
            
            ci.cancel();
        } else {
            AlphaEngineManager.clearPlayer(mcPlayer);
        }
    }
}
