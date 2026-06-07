package net.nostalgia.client.events.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.alphalogic.ritual.event.SkyPortalEvent;
import net.nostalgia.client.events.echo.MonolithicClientEchoRitualView;
import net.nostalgia.client.events.skyportal.MonolithicSkyPortalEvent;

@Environment(EnvType.CLIENT)
public final class ClientRitualEventRegistry {

    private ClientRitualEventRegistry() {}

    public static ClientEchoRitualView activeTransition() {
        return MonolithicClientEchoRitualView.activeOrNull();
    }

    public static SkyPortalEvent activeSkyPortal() {
        return MonolithicSkyPortalEvent.activeOrNull();
    }
}
