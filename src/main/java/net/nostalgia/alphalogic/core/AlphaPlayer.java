package net.nostalgia.alphalogic.core;

public class AlphaPlayer extends AlphaLivingEntity {
    public AlphaPlayer(AlphaWorld world) {
        super(world);
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
}
