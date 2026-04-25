package net.nostalgia.mixin.alpha;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import net.minecraft.sounds.SoundSource;

@Mixin(BowItem.class)
public abstract class AlphaBowMixin extends ProjectileWeaponItem {

    public AlphaBowMixin(Properties properties) {
        super(properties);
    }

    @Shadow
    public abstract int getDefaultProjectileRange();

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void nostalgia$alphaBowInstanteShoot(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(level).instantBowShoot) {
            ItemStack bowStack = player.getItemInHand(hand);
            boolean hasAmmo = !player.getProjectile(bowStack).isEmpty();
            boolean infinite = player.hasInfiniteMaterials();

            if (!hasAmmo && !infinite) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }

            ItemStack ammoStack = player.getProjectile(bowStack);
            if (ammoStack.isEmpty()) {
                ammoStack = new ItemStack(net.minecraft.world.item.Items.ARROW);
            }

            float power = 3.0F; 

            List<ItemStack> drawnAmmo = ProjectileWeaponItem.draw(bowStack, ammoStack, player);
            if (level instanceof ServerLevel serverLevel && !drawnAmmo.isEmpty()) {
                ItemStack ammo = drawnAmmo.get(0);
                if (!ammo.isEmpty()) {
                    
                    net.minecraft.world.entity.projectile.Projectile arrow = this.createProjectile(level, player, bowStack, ammo, true);
                    this.shootProjectile(player, arrow, 0, power, 1.0F, 0.0F, null);
                    serverLevel.addFreshEntity(arrow);
                }
            }

            level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                net.nostalgia.sound.AlphaSounds.RANDOM_BOW,
                SoundSource.PLAYERS,
                1.0F,
                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
            );
            player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));

            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
