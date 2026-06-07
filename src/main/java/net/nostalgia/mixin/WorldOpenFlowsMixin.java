package net.nostalgia.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {

    @Inject(method = "askForBackup", at = @At("HEAD"), cancellable = true)
    private void skipBackupConfirm(LevelStorageSource.LevelStorageAccess access, boolean custom, Runnable proceed,
            Runnable cancel, CallbackInfo ci) {
        
        proceed.run();
        ci.cancel();
    }

    @Inject(method = "confirmWorldCreation", at = @At("HEAD"), cancellable = true)
    private static void skipExperimentalWarning(Minecraft minecraft, CreateWorldScreen screen, Lifecycle lifecycle,
            Runnable proceed, boolean skipWarning, CallbackInfo ci) {
        
        proceed.run();
        ci.cancel();
    }
}
