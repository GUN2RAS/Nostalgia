package net.nostalgia.mixin.alpha;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.core.AlphaPlayer;
import net.nostalgia.world.dimension.ModDimensions;
import com.example.api.GravityChanger;
import com.example.api.Gravity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class AlphaPlayerBridgeMixin {

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void hijackAlphaTravel(Vec3 movementInput, CallbackInfo ci) {
        Player mcPlayer = (Player) (Object) this;

        if (mcPlayer.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY && !mcPlayer.isSpectator()) {
            if (mcPlayer.isSleeping()) {
                return;
            }
            if (mcPlayer instanceof GravityChanger changer) {
                boolean hasCustomGravity = changer.isInfected() || changer.getInfectedGravity() != Gravity.DOWN || changer.getGravityDirection() != Gravity.DOWN;
                if (hasCustomGravity) {
                    return;
                }
            }
            
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

                int prevStep = alphaP.nextStepDistance;

                alphaP.onUpdate();

                if (localPlayer.isPassenger()) {
                    alphaP.setPosition(localPlayer.getX(), localPlayer.getY() + alphaP.yOffset - alphaP.ySize, localPlayer.getZ());
                    alphaP.fallDistance = 0;
                } else {
                    localPlayer.setPos(alphaP.posX, alphaP.boundingBox.minY, alphaP.posZ);
                }

                if (alphaP.distanceWalkedModified > prevStep && alphaP.onGround) {
                    net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(
                        net.minecraft.util.Mth.floor(alphaP.posX),
                        net.minecraft.util.Mth.floor(alphaP.boundingBox.minY - 0.2),
                        net.minecraft.util.Mth.floor(alphaP.posZ)
                    );
                    net.minecraft.world.level.block.state.BlockState state = localPlayer.level().getBlockState(pos);
                    if (state.isAir()) {
                        String dimId = localPlayer.level().dimension().identifier().toString();
                        net.minecraft.world.level.block.state.BlockState holoState = net.nostalgia.client.events.caches.UniversalHologramCache.getBlockState(dimId, pos.getX(), pos.getY(), pos.getZ(), false);
                        if (holoState != null && !holoState.isAir()) {
                            state = holoState;
                        }
                    }
                    if (state.isAir()) {
                        net.minecraft.core.BlockPos posLegs = new net.minecraft.core.BlockPos(
                            net.minecraft.util.Mth.floor(alphaP.posX),
                            net.minecraft.util.Mth.floor(alphaP.boundingBox.minY),
                            net.minecraft.util.Mth.floor(alphaP.posZ)
                        );
                        net.minecraft.world.level.block.state.BlockState legsState = localPlayer.level().getBlockState(posLegs);
                        if (legsState.isAir()) {
                            String dimId = localPlayer.level().dimension().identifier().toString();
                            net.minecraft.world.level.block.state.BlockState holoLegs = net.nostalgia.client.events.caches.UniversalHologramCache.getBlockState(dimId, posLegs.getX(), posLegs.getY(), posLegs.getZ(), false);
                            if (holoLegs != null && !holoLegs.isAir()) {
                                legsState = holoLegs;
                            }
                        }
                        if (!legsState.isAir() && legsState.getFluidState().isEmpty()) {
                            state = legsState;
                            pos = posLegs;
                        }
                    }
                    if (!state.isAir()) {
                        net.minecraft.world.level.block.SoundType soundType = state.getSoundType();
                        localPlayer.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15f, soundType.getPitch());
                    }
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
