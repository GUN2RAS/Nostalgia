package net.nostalgia.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Method;

@Environment(EnvType.CLIENT)
public final class IrisCompat {
    private IrisCompat() {}

    private static final boolean IRIS_PRESENT = FabricLoader.getInstance().isModLoaded("iris");
    private static Method isShaderPackInUseMethod;
    private static Object apiInstance;
    private static boolean reflectionInitDone = false;
    private static boolean reflectionInitOk = false;

    public static boolean isShaderPackActive() {
        if (!IRIS_PRESENT) return false;
        if (!reflectionInitDone) initReflection();
        if (!reflectionInitOk) return false;
        try {
            Object r = isShaderPackInUseMethod.invoke(apiInstance);
            return r instanceof Boolean && (Boolean) r;
        } catch (Throwable t) {
            return false;
        }
    }

    private static void initReflection() {
        reflectionInitDone = true;
        try {
            Class<?> apiCls = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Method getInstance = apiCls.getMethod("getInstance");
            apiInstance = getInstance.invoke(null);
            isShaderPackInUseMethod = apiCls.getMethod("isShaderPackInUse");
            reflectionInitOk = true;
        } catch (Throwable t) {
            reflectionInitOk = false;
        }
    }
}
