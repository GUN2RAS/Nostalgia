package net.nostalgia.alphalogic.core;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class AlphaPlayer extends AlphaLivingEntity {
  public final Player mcPlayer;
  public boolean isSneaking;

  public AlphaPlayer(AlphaWorld world, Player mcPlayer) {
    super(world);
    this.mcPlayer = mcPlayer;
    this.yOffset = 1.62F;
    this.setSize(0.6F, 1.8F);
  }

  @Override
  public boolean isSneaking() {
    return this.isSneaking;
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    return !this.mcPlayer.level().isClientSide() && this.mcPlayer.level() instanceof ServerLevel serverLevel
      ? this.mcPlayer.hurtServer(serverLevel, source, amount)
      : false;
  }

  @Override
  public void playStepSound(int x, int y, int z, int blockId) {
    if (!this.mcPlayer.level().isClientSide()) {
      BlockPos pos = new BlockPos(x, y, z);
      BlockState state = this.mcPlayer.level().getBlockState(pos);
      SoundType soundType = state.getSoundType();
      this.mcPlayer.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
    }
  }
}
