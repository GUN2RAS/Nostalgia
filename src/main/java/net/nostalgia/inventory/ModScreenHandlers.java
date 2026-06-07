package net.nostalgia.inventory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlers {
    public static final MenuType<TimeMachineMenu> TIME_MACHINE_MENU = Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath("nostalgia", "time_machine_menu"),
            new MenuType<>(TimeMachineMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final MenuType<LodestoneGravityMenu> LODESTONE_GRAVITY_MENU = Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath("nostalgia", "lodestone_gravity_menu"),
            new MenuType<>(LodestoneGravityMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void register() {
        
    }
}
