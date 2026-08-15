/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class fr {
    private static final String[] y = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
    private static final String[] z = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
    public float a = 1.0f;
    public float b = 1.0f;
    public float c = 0.5f;
    public boolean d = false;
    public int e = 0;
    public boolean f = true;
    public boolean g = false;
    public boolean h = false;
    public boolean i = true;
    public it j = new it("Forward", 17);
    public it k = new it("Left", 30);
    public it l = new it("Back", 31);
    public it m = new it("Right", 32);
    public it n = new it("Jump", 57);
    public it o = new it("Inventory", 23);
    public it p = new it("Drop", 16);
    public it q = new it("Chat", 20);
    public it r = new it("Toggle fog", 33);
    public it s = new it("Sneak", 42);
    public it[] t = new it[]{this.j, this.k, this.l, this.m, this.n, this.s, this.p, this.o, this.q, this.r};
    protected Minecraft u;
    private File A;
    public int v = 10;
    public int w = 2;
    public boolean x = false;

    public fr(Minecraft minecraft, File file) {
        this.u = minecraft;
        this.A = new File(file, "options.txt");
        this.a();
    }

    public fr() {
    }

    public String a(int n2) {
        return this.t[n2].a + ": " + Keyboard.getKeyName((int)this.t[n2].b);
    }

    public void a(int n2, int n3) {
        this.t[n2].b = n3;
        this.b();
    }

    public void a(int n2, float f2) {
        if (n2 == 0) {
            this.a = f2;
            this.u.A.a();
        }
        if (n2 == 1) {
            this.b = f2;
            this.u.A.a();
        }
        if (n2 == 3) {
            this.c = f2;
        }
    }

    public void b(int n2, int n3) {
        if (n2 == 2) {
            boolean bl2 = this.d = !this.d;
        }
        if (n2 == 4) {
            this.e = this.e + n3 & 3;
        }
        if (n2 == 5) {
            boolean bl3 = this.f = !this.f;
        }
        if (n2 == 6) {
            this.g = !this.g;
            this.u.n.b();
        }
        if (n2 == 7) {
            boolean bl4 = this.h = !this.h;
        }
        if (n2 == 8) {
            this.w = this.w + n3 & 3;
        }
        if (n2 == 9) {
            this.i = !this.i;
            this.u.f.a();
        }
        this.b();
    }

    public int b(int n2) {
        if (n2 == 0) {
            return 1;
        }
        if (n2 == 1) {
            return 1;
        }
        if (n2 == 3) {
            return 1;
        }
        return 0;
    }

    public float c(int n2) {
        if (n2 == 0) {
            return this.a;
        }
        if (n2 == 1) {
            return this.b;
        }
        if (n2 == 3) {
            return this.c;
        }
        return 0.0f;
    }

    public String d(int n2) {
        if (n2 == 0) {
            return "Music: " + (this.a > 0.0f ? (int)(this.a * 100.0f) + "%" : "OFF");
        }
        if (n2 == 1) {
            return "Sound: " + (this.b > 0.0f ? (int)(this.b * 100.0f) + "%" : "OFF");
        }
        if (n2 == 2) {
            return "Invert mouse: " + (this.d ? "ON" : "OFF");
        }
        if (n2 == 3) {
            if (this.c == 0.0f) {
                return "Sensitivity: *yawn*";
            }
            if (this.c == 1.0f) {
                return "Sensitivity: HYPERSPEED!!!";
            }
            return "Sensitivity: " + (int)(this.c * 200.0f) + "%";
        }
        if (n2 == 4) {
            return "Render distance: " + y[this.e];
        }
        if (n2 == 5) {
            return "View bobbing: " + (this.f ? "ON" : "OFF");
        }
        if (n2 == 6) {
            return "3d anaglyph: " + (this.g ? "ON" : "OFF");
        }
        if (n2 == 7) {
            return "Limit framerate: " + (this.h ? "ON" : "OFF");
        }
        if (n2 == 8) {
            return "Difficulty: " + z[this.w];
        }
        if (n2 == 9) {
            return "Graphics: " + (this.i ? "FANCY" : "FAST");
        }
        return "";
    }

    public void a() {
        try {
            if (!this.A.exists()) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.A));
            String string = "";
            while ((string = bufferedReader.readLine()) != null) {
                String[] stringArray = string.split(":");
                if (stringArray[0].equals("music")) {
                    this.a = this.a(stringArray[1]);
                }
                if (stringArray[0].equals("sound")) {
                    this.b = this.a(stringArray[1]);
                }
                if (stringArray[0].equals("mouseSensitivity")) {
                    this.c = this.a(stringArray[1]);
                }
                if (stringArray[0].equals("invertYMouse")) {
                    this.d = stringArray[1].equals("true");
                }
                if (stringArray[0].equals("viewDistance")) {
                    this.e = Integer.parseInt(stringArray[1]);
                }
                if (stringArray[0].equals("bobView")) {
                    this.f = stringArray[1].equals("true");
                }
                if (stringArray[0].equals("anaglyph3d")) {
                    this.g = stringArray[1].equals("true");
                }
                if (stringArray[0].equals("limitFramerate")) {
                    this.h = stringArray[1].equals("true");
                }
                if (stringArray[0].equals("difficulty")) {
                    this.w = Integer.parseInt(stringArray[1]);
                }
                if (stringArray[0].equals("fancyGraphics")) {
                    this.i = stringArray[1].equals("true");
                }
                for (int i2 = 0; i2 < this.t.length; ++i2) {
                    if (!stringArray[0].equals("key_" + this.t[i2].a)) continue;
                    this.t[i2].b = Integer.parseInt(stringArray[1]);
                }
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            System.out.println("Failed to load options");
            exception.printStackTrace();
        }
    }

    private float a(String string) {
        if (string.equals("true")) {
            return 1.0f;
        }
        if (string.equals("false")) {
            return 0.0f;
        }
        return Float.parseFloat(string);
    }

    public void b() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(this.A));
            printWriter.println("music:" + this.a);
            printWriter.println("sound:" + this.b);
            printWriter.println("invertYMouse:" + this.d);
            printWriter.println("mouseSensitivity:" + this.c);
            printWriter.println("viewDistance:" + this.e);
            printWriter.println("bobView:" + this.f);
            printWriter.println("anaglyph3d:" + this.g);
            printWriter.println("limitFramerate:" + this.h);
            printWriter.println("difficulty:" + this.w);
            printWriter.println("fancyGraphics:" + this.i);
            for (int i2 = 0; i2 < this.t.length; ++i2) {
                printWriter.println("key_" + this.t[i2].a + ":" + this.t[i2].b);
            }
            printWriter.close();
        }
        catch (Exception exception) {
            System.out.println("Failed to save options");
            exception.printStackTrace();
        }
    }
}

