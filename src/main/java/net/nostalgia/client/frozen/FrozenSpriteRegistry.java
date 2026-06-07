package net.nostalgia.client.frozen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Environment(EnvType.CLIENT)
public final class FrozenSpriteRegistry {

    public static final String FROZEN_NAMESPACE = "nostalgia";
    public static final String FROZEN_PATH_PREFIX = "frozen/";

    private static final Map<Identifier, Identifier> LIVE_TO_FROZEN_ID = new ConcurrentHashMap<>();
    private static final Map<Identifier, TextureAtlasSprite> FROZEN_BY_ID = new ConcurrentHashMap<>();
    private static final Map<Identifier, TextureAtlasSprite> FROZEN_BY_LIVE_ID = new ConcurrentHashMap<>();

    private FrozenSpriteRegistry() {}

    public static Identifier toFrozenId(Identifier live) {
        return Identifier.fromNamespaceAndPath(
                FROZEN_NAMESPACE,
                FROZEN_PATH_PREFIX + live.getNamespace() + "/" + live.getPath()
        );
    }

    public static void registerMapping(Identifier live, Identifier frozen) {
        LIVE_TO_FROZEN_ID.put(live, frozen);
    }

    public static void registerSprite(Identifier frozenId, TextureAtlasSprite sprite) {
        FROZEN_BY_ID.put(frozenId, sprite);
    }

    public static void bindFrozenByLive() {
        Map<Identifier, TextureAtlasSprite> newMap = new HashMap<>();
        for (Map.Entry<Identifier, Identifier> e : LIVE_TO_FROZEN_ID.entrySet()) {
            TextureAtlasSprite frozen = FROZEN_BY_ID.get(e.getValue());
            if (frozen != null) newMap.put(e.getKey(), frozen);
        }
        FROZEN_BY_LIVE_ID.clear();
        FROZEN_BY_LIVE_ID.putAll(newMap);
    }

    public static TextureAtlasSprite getFrozenFor(TextureAtlasSprite liveSprite) {
        if (liveSprite == null) return null;
        Identifier liveId = liveSprite.contents().name();
        return FROZEN_BY_LIVE_ID.get(liveId);
    }

    public static boolean hasAnyMappings() {
        return !FROZEN_BY_LIVE_ID.isEmpty();
    }

    public static void clearAll() {
        LIVE_TO_FROZEN_ID.clear();
        FROZEN_BY_ID.clear();
        FROZEN_BY_LIVE_ID.clear();
    }
}
