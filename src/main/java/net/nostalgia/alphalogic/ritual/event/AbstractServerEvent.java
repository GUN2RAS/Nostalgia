package net.nostalgia.alphalogic.ritual.event;

import java.util.UUID;
import net.minecraft.server.level.ServerLevel;

public abstract class AbstractServerEvent {
    public final UUID eventId;
    protected ServerLevel level;

    public AbstractServerEvent(UUID eventId, ServerLevel level) {
        this.eventId = eventId;
        this.level = level;
    }

    public abstract void onStart();
    public abstract void tick();
    public abstract void onEnd();
}
