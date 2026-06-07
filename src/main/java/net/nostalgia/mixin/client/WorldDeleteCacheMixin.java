package net.nostalgia.mixin.client;

import net.minecraft.world.level.storage.LevelStorageSource;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public abstract class WorldDeleteCacheMixin {

    @Shadow @Final private String levelId;

    @Inject(method = "deleteLevel", at = @At("HEAD"))
    private void nostalgia$deleteCacheOnWorldDelete(CallbackInfo ci) {
        try {
            Path cacheDir = HologramDiskCache.getCacheFolderForLevel(this.levelId);
            if (Files.exists(cacheDir) && Files.isDirectory(cacheDir)) {
                Files.walkFileTree(cacheDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.deleteIfExists(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("[Nostalgia] Deleted cache for world: " + this.levelId + " at " + cacheDir);
            }
        } catch (Exception e) {
            System.err.println("[Nostalgia] Failed to delete cache for world: " + this.levelId);
            e.printStackTrace();
        }
    }
}
