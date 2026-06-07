package net.nostalgia.alphalogic.ritual.event;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

public class ServerEventManager {
    private static final ConcurrentHashMap<UUID, AbstractServerEvent> activeEvents = new ConcurrentHashMap<>();

    public static void startEvent(AbstractServerEvent event) {
        activeEvents.put(event.eventId, event);
        event.onStart();
    }

    public static void tickAll() {
        for (AbstractServerEvent event : activeEvents.values()) {
            event.tick();
        }
    }

    public static void endEvent(UUID eventId) {
        AbstractServerEvent event = activeEvents.remove(eventId);
        if (event != null) {
            event.onEnd();
        }
    }

    public static AbstractServerEvent getEvent(UUID eventId) {
        return activeEvents.get(eventId);
    }
    
    public static Collection<AbstractServerEvent> getAllEvents() {
        return activeEvents.values();
    }
    
    public static void clear() {
        for (AbstractServerEvent event : activeEvents.values()) {
            event.onEnd();
        }
        activeEvents.clear();
    }
}
