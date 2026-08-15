package net.nostalgia.entity;

import com.example.api.Gravity;
import com.example.api.GravityChanger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.nostalgia.item.ChargedAmethystItem;
import net.nostalgia.item.ModItems;

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

  protected Item getDefaultItem() {
    return Items.AMETHYST_SHARD;
  }

  private ParticleOptions getParticle() {
    ItemStack item = this.getItem();
    return item.isEmpty()
      ? new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(new ItemStack(Items.AMETHYST_SHARD)))
      : new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
  }

  public void handleEntityEvent(byte id) {
    if (id == 3) {
      ParticleOptions particle = this.getParticle();

      for (int i = 0; i < 8; i++) {
        this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
      }
    }
  }

  protected void onHitEntity(EntityHitResult hitResult) {
    super.onHitEntity(hitResult);
    Entity entity = hitResult.getEntity();
    entity.hurt(this.damageSources().thrown(this, this.getOwner()), 5.0F);
    if (!this.level().isClientSide()) {
      ItemStack stack = this.getItem();
      if (stack.is(ModItems.CHARGED_AMETHYST) && entity instanceof LivingEntity living) {
        String direction = ChargedAmethystItem.getDirection(stack);
        if (!"none".equals(direction) && living instanceof GravityChanger gc) {
          Gravity gravity = switch (direction) {
            case "up" -> Gravity.UP;
            case "down" -> Gravity.DOWN;
            case "left" -> Gravity.WEST;
            case "right" -> Gravity.EAST;
            default -> Gravity.DOWN;
          };
          gc.infect(gravity, 200);
          this.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0F, 0.8F);
        }
      }
    }
  }

  protected void onHit(HitResult hitResult) {
    super.onHit(hitResult);
    if (!this.level().isClientSide()) {
      this.level().broadcastEntityEvent(this, (byte)3);
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.2F);
      ItemStack stack = this.getItem();
      if (stack.is(ModItems.CHARGED_AMETHYST) && hitResult instanceof BlockHitResult blockHit) {
        String direction = ChargedAmethystItem.getDirection(stack);
        if (!"none".equals(direction)) {
          EntityType<?> fieldType = BuiltInRegistries.ENTITY_TYPE
            .get(Identifier.fromNamespaceAndPath("zemlya", "gravity_field"))
            .<EntityType<?>>map(Holder::value)
            .orElse(null);
          if (fieldType != null) {
            Entity field = fieldType.create(this.level(), EntitySpawnReason.TRIGGERED);
            if (field != null) {
              int gravityVal = switch (direction) {
                case "up" -> 1;
                case "down" -> 0;
                case "left" -> 4;
                case "right" -> 5;
                default -> 0;
              };
              CompoundTag nbt = new CompoundTag();
              nbt.putInt("FieldGravity", gravityVal);
              nbt.putInt("Lifetime", 200);
              nbt.putInt("LandingDirection", blockHit.getDirection().ordinal());
              BlockPos hitPos = blockHit.getBlockPos();
              ListTag posList = new ListTag();
              posList.add(DoubleTag.valueOf(hitPos.getX() + 0.5));
              posList.add(DoubleTag.valueOf(hitPos.getY() + 0.5));
              posList.add(DoubleTag.valueOf(hitPos.getZ() + 0.5));
              nbt.put("Pos", posList);
              field.load(TagValueInput.create(ProblemReporter.DISCARDING, this.level().registryAccess(), nbt));
              field.setPos(hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5);
              this.level().addFreshEntity(field);
              this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0F, 0.8F);
            }
          }
        }
      }

      this.discard();
    }
  }
}
