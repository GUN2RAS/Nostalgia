package net.nostalgia.client.events.caches.providers;

import net.minecraft.world.level.block.state.BlockState;

public class HologramSection {
    public BlockState[] palette;
    public byte[] indices;
    public net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome>[] biomePalette;
    public byte[] biomeIndices;

    private byte[] lazyData;
    private int lazyOffset;
    private boolean isLazy = false;

    public HologramSection(BlockState[] palette, byte[] indices, net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome>[] biomePalette, byte[] biomeIndices) {
        this.palette = palette;
        this.indices = indices;
        this.biomePalette = biomePalette;
        this.biomeIndices = biomeIndices;
    }

    public HologramSection(byte[] lazyData, int lazyOffset) {
        this.lazyData = lazyData;
        this.lazyOffset = lazyOffset;
        this.isLazy = true;
    }

    public void resolveLazy() {
        if (!isLazy) return;
        synchronized(this) {
            if (!isLazy) return;
            java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(lazyData, lazyOffset, lazyData.length - lazyOffset);
            
            int palSize = buf.getShort() & 0xFFFF;
            this.palette = new BlockState[palSize];
            for (int p = 0; p < palSize; p++) {
                this.palette[p] = net.minecraft.world.level.block.Block.stateById(buf.getInt());
            }
            if (palSize > 1) {
                this.indices = new byte[4096];
                buf.get(this.indices);
            }

            int biomePalSize = buf.getShort() & 0xFFFF;
            if (biomePalSize > 0) {
                this.biomePalette = new net.minecraft.core.Holder[biomePalSize];
                net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomeRegistry = net.minecraft.client.Minecraft.getInstance().level != null 
                    ? net.minecraft.client.Minecraft.getInstance().level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BIOME) : null;
                for (int p = 0; p < biomePalSize; p++) {
                    int biomeId = buf.getInt();
                    if (biomeRegistry != null) {
                        net.minecraft.world.level.biome.Biome b = biomeRegistry.byId(biomeId);
                        if (b != null) {
                            this.biomePalette[p] = biomeRegistry.wrapAsHolder(b);
                        } else {
                            this.biomePalette[p] = biomeRegistry.getAny().get();
                        }
                    }
                }
            }
            if (biomePalSize > 1) {
                this.biomeIndices = new byte[64];
                buf.get(this.biomeIndices);
            }
            
            this.lazyData = null;
            this.isLazy = false;
        }
    }


    public HologramSection(BlockState[] palette, byte[] indices) {
        this(palette, indices, null, null);
    }

    public BlockState getBlockState(int localX, int localY, int localZ) {
        resolveLazy();
        if (palette == null || palette.length == 0) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        if (palette.length == 1 || indices == null) return palette[0];
        
        int index = (localY << 8) | (localZ << 4) | localX;
        if (index < 0 || index >= 4096) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        
        int palIndex = indices[index] & 0xFF;
        if (palIndex >= palette.length) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        return palette[palIndex];
    }

    public net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> getBiome(int localX, int localY, int localZ) {
        resolveLazy();
        if (biomePalette == null || biomePalette.length == 0) return null;
        if (biomePalette.length == 1 || biomeIndices == null) return biomePalette[0];
        
        int biomeX = localX >> 2;
        int biomeY = localY >> 2;
        int biomeZ = localZ >> 2;
        int index = (biomeY << 4) | (biomeZ << 2) | biomeX;
        if (index < 0 || index >= 64) return biomePalette[0];
        
        int palIndex = biomeIndices[index] & 0xFF;
        if (palIndex >= biomePalette.length) return biomePalette[0];
        return biomePalette[palIndex];
    }
}
