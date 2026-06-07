package net.nostalgia.client.events.echo;

import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.ritual.event.ClientEchoRitualView;
import net.nostalgia.client.events.core.IHologramContext;
import net.nostalgia.client.events.core.ClientRitualEventRegistry;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class EchoTransitionContext implements IHologramContext {

    public static final EchoTransitionContext INSTANCE = new EchoTransitionContext();

    private EchoTransitionContext() {}

    @Override
    public boolean isActive() {
        ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
        if (transition == null) return false;

        boolean inNew = transition.isInNewDimension();
        if (inNew) {
            // Если мы уже перелетели (из Овера в Альфу), голограмма больше не нужна, мы в самом мире Альфы
            return false;
        }

        // Контекст активен с фазы 1 (когда маяк/якорь ломаются и записываются как дельта).
        // Ландшафт голограммы рендерится отдельно через isTerrainActive() (фаза >= 3).
        if (!transition.isBystander() && RitualVisualManager.currentPhase < 1) {
            return false;
        }

        return true;
    }

    @Override
    public boolean contains(int x, int y, int z) {
        ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
        if (transition == null) return false;

        BlockPos center = transition.ritualCenter();
        if (center == null) return false;

        float currentRadius = getCollisionRadius();
        if (currentRadius <= 0.01f) return false;

        double dx = x - center.getX();
        double dz = z - center.getZ();

        long h = (x * 73856093L) ^ (y * 19349663L) ^ (z * 83492791L);
        double noise = ((h & 0xFFFFFF) / (double) 0xFFFFFF) * 2.0 - 1.0;

        double distSqXZ = dx * dx + dz * dz;
        double threshold = currentRadius - (noise * 2.0);

        if (threshold < 0 || distSqXZ > threshold * threshold) {
            return false;
        }

        if (y < -64 || y > 320) {
            return false;
        }

        return true;
    }

    @Override
    public BlockPos getCenter() {
        ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
        return transition != null ? transition.ritualCenter() : null;
    }

    @Override
    public float getRadius() {
        ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
        return transition != null ? transition.alphaRadius() : 0.0f;
    }

    @Override
    public int getOffsetX() {
        return RitualEventRegistry.offsetX();
    }

    @Override
    public int getOffsetY() {
        return RitualEventRegistry.yOffset();
    }

    @Override
    public int getOffsetZ() {
        return RitualEventRegistry.offsetZ();
    }

    @Override
    public String getTargetDimension() {
        ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
        if (transition == null) return null;
        return net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(transition.targetDimension());
    }

    @Override
    public boolean isSkyInverted() {
        return false;
    }

    @Override
    public boolean isTerrainActive() {
        ClientEchoRitualView transition = ClientRitualEventRegistry.activeTransition();
        return transition != null && transition.currentPhase() >= 3;
    }

    @Override
    public float getCollisionRadius() {
        if (net.nostalgia.client.events.caches.UniversalHologramCache.decoupledCollision) {
            return net.nostalgia.client.events.caches.UniversalHologramCache.customCollisionRadius;
        }
        return getRadius();
    }
}
