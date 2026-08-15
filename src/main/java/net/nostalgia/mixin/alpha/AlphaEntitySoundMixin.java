package net.nostalgia.mixin.alpha;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.nostalgia.world.rules.DimensionRules;
import net.nostalgia.world.rules.NostalgiaDimensionRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class AlphaEntitySoundMixin {
    @ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private SoundEvent nostalgia$redirectSound(SoundEvent sound) {
        Entity entity = (Entity) (Object) this;
        if (entity.level() != null) {
            DimensionRules rules = NostalgiaDimensionRules.getRules(entity.level());
            if (rules != null) {
                SoundEvent redirected = rules.getRedirectedSound(entity.getType(), sound);
                if (redirected != null) {
                    return redirected;
                }
            }
        }
        return sound;
    }
}
