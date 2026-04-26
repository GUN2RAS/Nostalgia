package net.nostalgia.alphalogic.core;

import net.minecraft.world.entity.player.Player;

public class AlphaPlayer extends AlphaLivingEntity {
    public final Player mcPlayer;

    public AlphaPlayer(AlphaWorld world, Player mcPlayer) {
        super(world);
        this.mcPlayer = mcPlayer;
        this.yOffset = 1.62f; 
        this.setSize(0.6f, 1.8f);
    }

    public boolean isSneaking;

    @Override
    public boolean isSneaking() {
        return this.isSneaking;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        if (!this.mcPlayer.level().isClientSide() && this.mcPlayer.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            return this.mcPlayer.hurtServer(serverLevel, source, amount);
        }
        return false;
    }

    @Override
    public void playStepSound(int x, int y, int z, int blockId) {
        if (!this.mcPlayer.level().isClientSide()) {
            net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(x, y, z);
            net.minecraft.world.level.block.state.BlockState state = this.mcPlayer.level().getBlockState(pos);
            net.minecraft.world.level.block.SoundType soundType = state.getSoundType();
            this.mcPlayer.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15f, soundType.getPitch());
        }
    }
}
