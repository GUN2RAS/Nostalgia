package net.nostalgia.client.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.nostalgia.alphalogic.ritual.event.ClientTransitionView;

@Environment(EnvType.CLIENT)
public final class ClientRitualEventRegistry {

    private ClientRitualEventRegistry() {}

    public static ClientTransitionView activeTransition() {
        return MonolithicClientTransitionView.activeOrNull();
    }
}
