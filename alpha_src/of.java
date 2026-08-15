/*
 * Decompiled with CFR 0.152.
 */
import java.io.File;
import java.util.Random;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class of {
    private static SoundSystem a;
    private eb b = new eb();
    private eb c = new eb();
    private eb d = new eb();
    private int e = 0;
    private fr f;
    private static boolean g;
    private Random h = new Random();
    private int i = this.h.nextInt(12000);

    public void a(fr fr2) {
        this.c.b = false;
        this.f = fr2;
        if (!(g || fr2 != null && fr2.b == 0.0f && fr2.a == 0.0f)) {
            this.d();
        }
    }

    private void d() {
        try {
            float f2 = this.f.b;
            float f3 = this.f.a;
            this.f.b = 0.0f;
            this.f.a = 0.0f;
            this.f.b();
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
            SoundSystemConfig.setCodec("mus", ep.class);
            SoundSystemConfig.setCodec("wav", CodecWav.class);
            a = new SoundSystem();
            this.f.b = f2;
            this.f.a = f3;
            this.f.b();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }
        g = true;
    }

    public void a() {
        if (!(g || this.f.b == 0.0f && this.f.a == 0.0f)) {
            this.d();
        }
        if (this.f.a == 0.0f) {
            a.stop("BgMusic");
        } else {
            a.setVolume("BgMusic", this.f.a);
        }
    }

    public void b() {
        if (g) {
            a.cleanup();
        }
    }

    public void a(String string, File file) {
        this.b.a(string, file);
    }

    public void b(String string, File file) {
        this.c.a(string, file);
    }

    public void c(String string, File file) {
        this.d.a(string, file);
    }

    public void c() {
        if (!g || this.f.a == 0.0f) {
            return;
        }
        if (!a.playing("BgMusic") && !a.playing("streaming")) {
            if (this.i > 0) {
                --this.i;
                return;
            }
            ah ah2 = this.d.a();
            if (ah2 != null) {
                this.i = this.h.nextInt(24000) + 24000;
                a.backgroundMusic("BgMusic", ah2.b, ah2.a, false);
                a.setVolume("BgMusic", this.f.a);
                a.play("BgMusic");
            }
        }
    }

    public void a(ge ge2, float f2) {
        if (!g || this.f.b == 0.0f) {
            return;
        }
        if (ge2 == null) {
            return;
        }
        float f3 = ge2.as + (ge2.aq - ge2.as) * f2;
        double d2 = ge2.ah + (ge2.ak - ge2.ah) * (double)f2;
        double d3 = ge2.ai + (ge2.al - ge2.ai) * (double)f2;
        double d4 = ge2.aj + (ge2.am - ge2.aj) * (double)f2;
        float f4 = eo.b(-f3 * ((float)Math.PI / 180) - (float)Math.PI);
        float f5 = eo.a(-f3 * ((float)Math.PI / 180) - (float)Math.PI);
        float f6 = -f5;
        float f7 = 0.0f;
        float f8 = -f4;
        float f9 = 0.0f;
        float f10 = 1.0f;
        float f11 = 0.0f;
        a.setListenerPosition((float)d2, (float)d3, (float)d4);
        a.setListenerOrientation(f6, f7, f8, f9, f10, f11);
    }

    public void a(String string, float f2, float f3, float f4, float f5, float f6) {
        if (!g || this.f.b == 0.0f) {
            return;
        }
        String string2 = "streaming";
        if (a.playing("streaming")) {
            a.stop("streaming");
        }
        if (string == null) {
            return;
        }
        ah ah2 = this.c.a(string);
        if (ah2 != null && f5 > 0.0f) {
            if (a.playing("BgMusic")) {
                a.stop("BgMusic");
            }
            float f7 = 16.0f;
            a.newStreamingSource(true, string2, ah2.b, ah2.a, false, f2, f3, f4, 2, f7 * 4.0f);
            a.setVolume(string2, 0.5f * this.f.b);
            a.play(string2);
        }
    }

    public void b(String string, float f2, float f3, float f4, float f5, float f6) {
        if (!g || this.f.b == 0.0f) {
            return;
        }
        ah ah2 = this.b.a(string);
        if (ah2 != null && f5 > 0.0f) {
            this.e = (this.e + 1) % 256;
            String string2 = "sound_" + this.e;
            float f7 = 16.0f;
            if (f5 > 1.0f) {
                f7 *= f5;
            }
            a.newSource(f5 > 1.0f, string2, ah2.b, ah2.a, false, f2, f3, f4, 2, f7);
            a.setPitch(string2, f6);
            if (f5 > 1.0f) {
                f5 = 1.0f;
            }
            a.setVolume(string2, f5 * this.f.b);
            a.play(string2);
        }
    }

    public void a(String string, float f2, float f3) {
        if (!g || this.f.b == 0.0f) {
            return;
        }
        ah ah2 = this.b.a(string);
        if (ah2 != null) {
            this.e = (this.e + 1) % 256;
            String string2 = "sound_" + this.e;
            a.newSource(false, string2, ah2.b, ah2.a, false, 0.0f, 0.0f, 0.0f, 0, 0.0f);
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            a.setPitch(string2, f3);
            a.setVolume(string2, (f2 *= 0.25f) * this.f.b);
            a.play(string2);
        }
    }

    static {
        g = false;
    }
}

