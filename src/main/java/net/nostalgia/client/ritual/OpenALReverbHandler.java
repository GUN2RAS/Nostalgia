package net.nostalgia.client.ritual;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

public class OpenALReverbHandler {

    private static boolean initialized = false;
    private static int auxFXSlot = 0;
    private static int reverbEffect = 0;
    private static int sendFilter = 0;

    public static void initialize() {
        if (initialized) {
            try {
                if (EXTEfx.alIsEffect(reverbEffect)) return;
            } catch (Exception e) {}
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
            EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL11.AL_TRUE);

            reverbEffect = EXTEfx.alGenEffects();
            EXTEfx.alEffecti(reverbEffect, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);

            sendFilter = EXTEfx.alGenFilters();
            EXTEfx.alFilteri(sendFilter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);

            
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DENSITY, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DIFFUSION, 1.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_GAIN, 0.5f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_GAINHF, 0.1f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DECAY_TIME, 5.0f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_DECAY_HFRATIO, 0.1f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN, 0.5f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY, 0.05f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN, 1.5f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, 0.05f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, 0.99f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, 0.1f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_ECHO_TIME, 0.25f);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_EAXREVERB_ECHO_DEPTH, 0.3f);

            EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverbEffect);

            initialized = true;
            System.out.println("[Nostalgia] EFX Reverb system initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applyReverb(int sourceId) {
        if (!initialized) return;
        try {
            if (!EXTEfx.alIsEffect(reverbEffect)) {
                initialized = false;
                initialize();
                if (!initialized) return;
            }
            EXTEfx.alFilterf(sendFilter, EXTEfx.AL_LOWPASS_GAIN, 1.0f);
            EXTEfx.alFilterf(sendFilter, EXTEfx.AL_LOWPASS_GAINHF, 0.5f);
            AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxFXSlot, 0, sendFilter);
        } catch (Exception e) {
            initialized = false;
        }
    }

    public static void removeReverb(int sourceId) {
        if (!initialized) return;
        try {
            if (!EXTEfx.alIsEffect(reverbEffect)) {
                initialized = false;
                return;
            }
            AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 0, EXTEfx.AL_FILTER_NULL);
        } catch (Exception e) {
            initialized = false;
        }
    }
}
