package net.nostalgia.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.client.events.echo.RitualVisualManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleBlockUpdate", at = @At("RETURN"))
    private void onBlockUpdate(ClientboundBlockUpdatePacket packet, CallbackInfo ci) {
        if (RitualVisualManager.isTransitioning && !RitualVisualManager.isBystander) {
            BlockPos pos = packet.getPos();
            BlockState state = packet.getBlockState();

            net.minecraft.client.Minecraft.getInstance().execute(() -> {
                if (net.minecraft.client.Minecraft.getInstance().level != null) {
                    String currentDim = net.minecraft.client.Minecraft.getInstance().level.dimension().identifier().toString();
                    net.nostalgia.client.events.caches.providers.DimensionHologramCache cache = net.nostalgia.client.events.caches.providers.DimensionHologramRegistry.getByName(currentDim);
                    if (cache != null) {
                        cache.setOverride(pos, state);
                    }
                }
                net.sha.api.SHAHologramManager.markAreaDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
            });
        }
    }
    @Inject(method = "handleChunkBlocksUpdate", at = @At("RETURN"))
    private void onChunkBlocksUpdate(ClientboundSectionBlocksUpdatePacket packet, CallbackInfo ci) {
        if (RitualVisualManager.isTransitioning && !RitualVisualManager.isBystander) {
            java.util.List<java.util.Map.Entry<BlockPos, BlockState>> updates = new java.util.ArrayList<>();
            packet.runUpdates((pos, state) -> updates.add(java.util.Map.entry(pos.immutable(), state)));
            net.minecraft.client.Minecraft.getInstance().execute(() -> {
                for (java.util.Map.Entry<BlockPos, BlockState> entry : updates) {
                    BlockPos pos = entry.getKey();
                    BlockState state = entry.getValue();
                    if (net.minecraft.client.Minecraft.getInstance().level != null) {
                        String currentDim = net.minecraft.client.Minecraft.getInstance().level.dimension().identifier().toString();
                        net.nostalgia.client.events.caches.providers.DimensionHologramCache cache = net.nostalgia.client.events.caches.providers.DimensionHologramRegistry.getByName(currentDim);
                        if (cache != null) {
                            cache.setOverride(pos, state);
                        }
                    }
                    net.sha.api.SHAHologramManager.markAreaDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
                }
            });
        }
    }
}
