package net.nostalgia.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.nostalgia.NostalgiaMod;

public class AlphaEntities {

    public static final EntityType<AlphaBoatEntity> ALPHA_BOAT = register(
            "alpha_boat",
            EntityType.Builder.<AlphaBoatEntity>of(AlphaBoatEntity::new, MobCategory.MISC)
                    .sized(1.5F, 0.6F)
                    .clientTrackingRange(10)
                    .updateInterval(3)
    );

    public static final EntityType<SkyPortalBeamEntity> SKY_PORTAL_BEAM = register(
            "sky_portal_beam",
            EntityType.Builder.<SkyPortalBeamEntity>of(SkyPortalBeamEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(256)
                    .updateInterval(20)
    );

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void register() {
    }
}
