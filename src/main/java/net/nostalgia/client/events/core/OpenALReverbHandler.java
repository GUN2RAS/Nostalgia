package net.nostalgia.client.events.core;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

public class OpenALReverbHandler {
  private static boolean initialized = false;
  private static int auxFXSlot = 0;
  private static int reverbEffect = 0;
  private static int sendFilter = 0;

  public OpenALReverbHandler() {
  }

  public static void initialize() {
    if (initialized) {
      try {
        if (EXTEfx.alIsEffect(reverbEffect)) {
          return;
        }
      } catch (Exception var5) {
      }

      initialized = false;
    }

    try {
      long currentContext = ALC10.alcGetCurrentContext();
      long device = ALC10.alcGetContextsDevice(currentContext);
      if (!ALC10.alcIsExtensionPresent(device, "ALC_EXT_EFX")) {
        System.out.println("[Nostalgia] EFX Extension not available - reverb disabled");
        return;
      }

      auxFXSlot = EXTEfx.alGenAuxiliaryEffectSlots();
      EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, 3, 1);
      reverbEffect = EXTEfx.alGenEffects();
      EXTEfx.alEffecti(reverbEffect, 32769, 32768);
      sendFilter = EXTEfx.alGenFilters();
      EXTEfx.alFilteri(sendFilter, 32769, 1);
      EXTEfx.alEffectf(reverbEffect, 1, 1.0F);
      EXTEfx.alEffectf(reverbEffect, 2, 1.0F);
      EXTEfx.alEffectf(reverbEffect, 3, 0.5F);
      EXTEfx.alEffectf(reverbEffect, 4, 0.1F);
      EXTEfx.alEffectf(reverbEffect, 6, 5.0F);
      EXTEfx.alEffectf(reverbEffect, 7, 0.1F);
      EXTEfx.alEffectf(reverbEffect, 9, 0.5F);
      EXTEfx.alEffectf(reverbEffect, 10, 0.05F);
      EXTEfx.alEffectf(reverbEffect, 12, 1.5F);
      EXTEfx.alEffectf(reverbEffect, 13, 0.05F);
      EXTEfx.alEffectf(reverbEffect, 19, 0.99F);
      EXTEfx.alEffectf(reverbEffect, 22, 0.1F);
      EXTEfx.alEffectf(reverbEffect, 15, 0.25F);
      EXTEfx.alEffectf(reverbEffect, 16, 0.3F);
      EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, 1, reverbEffect);
      initialized = true;
      System.out.println("[Nostalgia] EFX Reverb system initialized successfully");
    } catch (Exception var4) {
      var4.printStackTrace();
    }
  }

  public static void applyReverb(int sourceId) {
    if (initialized) {
      try {
        if (!EXTEfx.alIsEffect(reverbEffect)) {
          initialized = false;
          initialize();
          if (!initialized) {
            return;
          }
        }

        EXTEfx.alFilterf(sendFilter, 1, 1.0F);
        EXTEfx.alFilterf(sendFilter, 2, 0.5F);
        AL11.alSource3i(sourceId, 131078, auxFXSlot, 0, sendFilter);
      } catch (Exception var2) {
        initialized = false;
      }
    }
  }

  public static void removeReverb(int sourceId) {
    if (initialized) {
      try {
        if (!EXTEfx.alIsEffect(reverbEffect)) {
          initialized = false;
          return;
        }

        AL11.alSource3i(sourceId, 131078, 0, 0, 0);
      } catch (Exception var2) {
        initialized = false;
      }
    }
  }
}
