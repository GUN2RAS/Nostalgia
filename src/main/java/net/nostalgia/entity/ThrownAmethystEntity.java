package net.nostalgia.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownAmethystEntity extends ThrowableItemProjectile {

    public ThrownAmethystEntity(EntityType<? extends ThrownAmethystEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownAmethystEntity(Level level, LivingEntity shooter, ItemStack itemStack) {
        super(AlphaEntities.THROWN_AMETHYST, shooter, level, itemStack);
    }

    public ThrownAmethystEntity(Level level, double x, double y, double z, ItemStack itemStack) {
        super(AlphaEntities.THROWN_AMETHYST, x, y, z, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AMETHYST_SHARD;
    }

    private ParticleOptions getParticle() {
        ItemStack item = this.getItem();
        return (ParticleOptions)(item.isEmpty()
                ? new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(new ItemStack(Items.AMETHYST_SHARD)))
                : new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item)));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = this.getParticle();
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity entity = hitResult.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 5.0F);

        if (!this.level().isClientSide()) {
            ItemStack stack = this.getItem();
            if (stack.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST) && entity instanceof LivingEntity living) {
                String direction = net.nostalgia.item.ChargedAmethystItem.getDirection(stack);
                if (!"none".equals(direction) && living instanceof com.example.api.GravityChanger gc) {
                    com.example.api.Gravity gravity = switch (direction) {
                        case "up" -> com.example.api.Gravity.UP;
                        case "down" -> com.example.api.Gravity.DOWN;
                        case "left" -> com.example.api.Gravity.WEST;
                        case "right" -> com.example.api.Gravity.EAST;
                        default -> com.example.api.Gravity.DOWN;
                    };
                    gc.infect(gravity, 200);
                    this.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0F, 0.8F);
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.2F);
            
            ItemStack stack = this.getItem();
            if (stack.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST) && hitResult instanceof net.minecraft.world.phys.BlockHitResult blockHit) {
                String direction = net.nostalgia.item.ChargedAmethystItem.getDirection(stack);
                if (!"none".equals(direction)) {
                    net.minecraft.world.entity.EntityType<?> fieldType = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(
                        net.minecraft.resources.Identifier.fromNamespaceAndPath("zemlya", "gravity_field")
                    ).map(net.minecraft.core.Holder::value).orElse(null);
                    if (fieldType != null) {
                        net.minecraft.world.entity.Entity field = fieldType.create(this.level(), net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
                        if (field != null) {
                            int gravityVal = switch (direction) {
                                case "up" -> 1;
                                case "down" -> 0;
                                case "left" -> 4;
                                case "right" -> 5;
                                default -> 0;
                            };
                            
                            net.minecraft.nbt.CompoundTag nbt = new net.minecraft.nbt.CompoundTag();
                            nbt.putInt("FieldGravity", gravityVal);
                            nbt.putInt("Lifetime", 200);
                            nbt.putInt("LandingDirection", blockHit.getDirection().ordinal());
                            
                            net.minecraft.core.BlockPos hitPos = blockHit.getBlockPos();
                            net.minecraft.nbt.ListTag posList = new net.minecraft.nbt.ListTag();
                            posList.add(net.minecraft.nbt.DoubleTag.valueOf(hitPos.getX() + 0.5D));
                            posList.add(net.minecraft.nbt.DoubleTag.valueOf(hitPos.getY() + 0.5D));
                            posList.add(net.minecraft.nbt.DoubleTag.valueOf(hitPos.getZ() + 0.5D));
                            nbt.put("Pos", posList);
                            
                            field.load(net.minecraft.world.level.storage.TagValueInput.create(
                                     net.minecraft.util.ProblemReporter.DISCARDING,
                                     this.level().registryAccess(),
                                     nbt
                            ));
                            
                            field.setPos(hitPos.getX() + 0.5D, hitPos.getY() + 0.5D, hitPos.getZ() + 0.5D);
                            this.level().addFreshEntity(field);
                            
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), 
                                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0F, 0.8F);
                        }
                    }
                }
            }
            
            this.discard();
        }
    }
}
