package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class FakeBeaconBeamMixin {
    @Inject(method = "getBeamSections", at = @At("HEAD"), cancellable = true)
    private void forceRitualBeam(CallbackInfoReturnable<List<BeaconBeamOwner.Section>> cir) {
        BeaconBlockEntity beacon = (BeaconBlockEntity) (Object) this;
        Level level = beacon.getLevel();
        BlockPos pos = beacon.getBlockPos();
        
        if (level != null && level.isClientSide() && net.nostalgia.client.ritual.ClientFreezeRegions.isRitualBeacon(pos)) {
            List<BeaconBeamOwner.Section> fakeBeam = new ArrayList<>();
            BeaconBeamOwner.Section section = new BeaconBeamOwner.Section(net.minecraft.util.ARGB.color(255, 255, 255, 255));
            for (int i = 0; i < 384; i++) {
                section.increaseHeight();
            }
            fakeBeam.add(section);
            cir.setReturnValue(fakeBeam);
        }
    }
}
