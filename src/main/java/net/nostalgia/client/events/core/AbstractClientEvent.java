package net.nostalgia.client.events.core;

import java.util.UUID;

public abstract class AbstractClientEvent {
    public final UUID eventId;

    public AbstractClientEvent(UUID eventId) {
        this.eventId = eventId;
    }

    public abstract void onStart();
    public abstract void tick();
    public abstract void onEnd();
}
