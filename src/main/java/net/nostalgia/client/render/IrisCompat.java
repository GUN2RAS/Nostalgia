package net.nostalgia.client.render;

import java.lang.reflect.Method;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public final class IrisCompat {
  private static final boolean IRIS_PRESENT = FabricLoader.getInstance().isModLoaded("iris");
  private static Method isShaderPackInUseMethod;
  private static Object apiInstance;
  private static boolean reflectionInitDone = false;
  private static boolean reflectionInitOk = false;

  private IrisCompat() {
  }

  public static boolean isShaderPackActive() {
    if (!IRIS_PRESENT) {
      return false;
    } else {
      if (!reflectionInitDone) {
        initReflection();
      }

      if (!reflectionInitOk) {
        return false;
      } else {
        try {
          Object r = isShaderPackInUseMethod.invoke(apiInstance);
          return r instanceof Boolean && (Boolean)r;
        } catch (Throwable var1) {
          return false;
        }
      }
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
    } catch (Throwable var2) {
      reflectionInitOk = false;
    }
  }
}
