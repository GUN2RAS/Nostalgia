package net.nostalgia.client.render;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import org.joml.Matrix4f;

public class UboShaderUtil {

    public static Matrix4f getInverseViewProjMatrix(Camera camera, Matrix4f localCapturedProj) {
        if (PortalSkyRenderer.capturedProjectionMatrix != null && PortalSkyRenderer.capturedModelViewMatrix != null) {
            return new Matrix4f(PortalSkyRenderer.capturedProjectionMatrix).mul(PortalSkyRenderer.capturedModelViewMatrix).invert();
        } else if (localCapturedProj != null) {
            return new Matrix4f(localCapturedProj).mul(camera.getViewRotationMatrix(new Matrix4f())).invert();
        } else {
            return camera.getViewRotationProjectionMatrix(new Matrix4f()).invert();
        }
    }

    public static float getShaderTimeSeconds(DeltaTracker tracker) {
        return tracker.getGameTimeDeltaTicks() / 20.0f;
    }
}
