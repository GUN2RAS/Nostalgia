package net.nostalgia.client.events.core;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

public class ClientEventManager {
    private static final ConcurrentHashMap<UUID, AbstractClientEvent> activeEvents = new ConcurrentHashMap<>();

    public static void startEvent(AbstractClientEvent event) {
        activeEvents.put(event.eventId, event);
        event.onStart();
    }

    public static void tickAll() {
        for (AbstractClientEvent event : activeEvents.values()) {
            event.tick();
        }
    }

    public static void endEvent(UUID eventId) {
        AbstractClientEvent event = activeEvents.remove(eventId);
        if (event != null) {
            event.onEnd();
        }
    }

    public static AbstractClientEvent getEvent(UUID eventId) {
        return activeEvents.get(eventId);
    }
    
    public static Collection<AbstractClientEvent> getAllEvents() {
        return activeEvents.values();
    }
    
    public static void clear() {
        for (AbstractClientEvent event : activeEvents.values()) {
            event.onEnd();
        }
        activeEvents.clear();
    }
}
