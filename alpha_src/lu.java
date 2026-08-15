/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class lu
extends lm {
    private static ab d = new ab();
    private List e = new ArrayList();
    private Random f = new Random();
    private Minecraft g;
    public String a = null;
    private int h = 0;
    private String i = "";
    private int j = 0;
    public float b;
    float c = 1.0f;

    public lu(Minecraft minecraft) {
        this.g = minecraft;
    }

    public void a(float f2, boolean bl2, int n2, int n3) {
        String string;
        int n4;
        int n5;
        int n6;
        boolean bl3;
        iy iy2 = new iy(this.g.c, this.g.d);
        int n7 = iy2.a();
        int n8 = iy2.b();
        kd kd2 = this.g.o;
        this.g.r.b();
        GL11.glEnable((int)3042);
        if (this.g.y.i) {
            this.a(this.g.g.a(f2), n7, n8);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glBindTexture((int)3553, (int)this.g.n.a("/gui/gui.png"));
        eu eu2 = this.g.g.b;
        this.k = -90.0f;
        this.b(n7 / 2 - 91, n8 - 22, 0, 0, 182, 22);
        this.b(n7 / 2 - 91 - 1 + eu2.d * 20, n8 - 22 - 1, 0, 22, 24, 22);
        GL11.glBindTexture((int)3553, (int)this.g.n.a("/gui/icons.png"));
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)775, (int)769);
        this.b(n7 / 2 - 7, n8 / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable((int)3042);
        boolean bl4 = bl3 = this.g.g.aW / 3 % 2 == 1;
        if (this.g.g.aW < 10) {
            bl3 = false;
        }
        int n9 = this.g.g.E;
        int n10 = this.g.g.F;
        this.f.setSeed(this.h * 312871);
        if (this.g.b.d()) {
            int n11;
            n6 = this.g.g.m();
            for (n5 = 0; n5 < 10; ++n5) {
                n4 = n8 - 32;
                if (n6 > 0) {
                    n11 = n7 / 2 + 91 - n5 * 8 - 9;
                    if (n5 * 2 + 1 < n6) {
                        this.b(n11, n4, 34, 9, 9, 9);
                    }
                    if (n5 * 2 + 1 == n6) {
                        this.b(n11, n4, 25, 9, 9, 9);
                    }
                    if (n5 * 2 + 1 > n6) {
                        this.b(n11, n4, 16, 9, 9, 9);
                    }
                }
                n11 = 0;
                if (bl3) {
                    n11 = 1;
                }
                int n12 = n7 / 2 - 91 + n5 * 8;
                if (n9 <= 4) {
                    n4 += this.f.nextInt(2);
                }
                this.b(n12, n4, 16 + n11 * 9, 0, 9, 9);
                if (bl3) {
                    if (n5 * 2 + 1 < n10) {
                        this.b(n12, n4, 70, 0, 9, 9);
                    }
                    if (n5 * 2 + 1 == n10) {
                        this.b(n12, n4, 79, 0, 9, 9);
                    }
                }
                if (n5 * 2 + 1 < n9) {
                    this.b(n12, n4, 52, 0, 9, 9);
                }
                if (n5 * 2 + 1 != n9) continue;
                this.b(n12, n4, 61, 0, 9, 9);
            }
            if (this.g.g.a(gb.f)) {
                n5 = (int)Math.ceil((double)(this.g.g.aX - 2) * 10.0 / 300.0);
                n4 = (int)Math.ceil((double)this.g.g.aX * 10.0 / 300.0) - n5;
                for (n11 = 0; n11 < n5 + n4; ++n11) {
                    if (n11 < n5) {
                        this.b(n7 / 2 - 91 + n11 * 8, n8 - 32 - 9, 16, 18, 9, 9);
                        continue;
                    }
                    this.b(n7 / 2 - 91 + n11 * 8, n8 - 32 - 9, 25, 18, 9, 9);
                }
            }
        }
        GL11.glDisable((int)3042);
        GL11.glEnable((int)32826);
        GL11.glPushMatrix();
        GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        i.b();
        GL11.glPopMatrix();
        for (n6 = 0; n6 < 9; ++n6) {
            n5 = n7 / 2 - 90 + n6 * 20 + 2;
            n4 = n8 - 16 - 3;
            this.a(n6, n5, n4, f2);
        }
        i.a();
        GL11.glDisable((int)32826);
        if (Keyboard.isKeyDown((int)61)) {
            kd2.a("Minecraft Alpha v1.1.2_01 (" + this.g.G + ")", 2, 2, 0xFFFFFF);
            kd2.a(this.g.l(), 2, 12, 0xFFFFFF);
            kd2.a(this.g.m(), 2, 22, 0xFFFFFF);
            kd2.a(this.g.n(), 2, 32, 0xFFFFFF);
            long l2 = Runtime.getRuntime().maxMemory();
            long l3 = Runtime.getRuntime().totalMemory();
            long l4 = Runtime.getRuntime().freeMemory();
            long l5 = l3 - l4;
            string = "Used memory: " + l5 * 100L / l2 + "% (" + l5 / 1024L / 1024L + "MB) of " + l2 / 1024L / 1024L + "MB";
            this.b(kd2, string, n7 - kd2.a(string) - 2, 2, 0xE0E0E0);
            string = "Allocated memory: " + l3 * 100L / l2 + "% (" + l3 / 1024L / 1024L + "MB)";
            this.b(kd2, string, n7 - kd2.a(string) - 2, 12, 0xE0E0E0);
        } else {
            kd2.a("Minecraft Alpha v1.1.2_01", 2, 2, 0xFFFFFF);
        }
        if (this.j > 0) {
            float f3 = (float)this.j - f2;
            n5 = (int)(f3 * 256.0f / 20.0f);
            if (n5 > 255) {
                n5 = 255;
            }
            if (n5 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(n7 / 2), (float)(n8 - 48), (float)0.0f);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                int n13 = Color.HSBtoRGB(f3 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF;
                kd2.b(this.i, -kd2.a(this.i) / 2, -4, n13 + (n5 << 24));
                GL11.glDisable((int)3042);
                GL11.glPopMatrix();
            }
        }
        int n14 = 10;
        n5 = 0;
        if (this.g.p instanceof de) {
            n14 = 20;
            n5 = 1;
        }
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3008);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)0.0f, (float)(n8 - 48), (float)0.0f);
        for (int i2 = 0; i2 < this.e.size() && i2 < n14; ++i2) {
            if (((ko)this.e.get((int)i2)).b >= 200 && n5 == 0) continue;
            double d2 = (double)((ko)this.e.get((int)i2)).b / 200.0;
            d2 = 1.0 - d2;
            if ((d2 *= 10.0) < 0.0) {
                d2 = 0.0;
            }
            if (d2 > 1.0) {
                d2 = 1.0;
            }
            d2 *= d2;
            int n15 = (int)(255.0 * d2);
            if (n5 != 0) {
                n15 = 255;
            }
            if (n15 <= 0) continue;
            int n16 = 2;
            int n17 = -i2 * 9;
            string = ((ko)this.e.get((int)i2)).a;
            this.a(n16, n17 - 1, n16 + 320, n17 + 8, n15 / 2 << 24);
            GL11.glEnable((int)3042);
            kd2.a(string, n16, n17, 0xFFFFFF + (n15 << 24));
        }
        GL11.glPopMatrix();
        GL11.glEnable((int)3008);
        GL11.glDisable((int)3042);
    }

    private void a(float f2, int n2, int n3) {
        if ((f2 = 1.0f - f2) < 0.0f) {
            f2 = 0.0f;
        }
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        this.c = (float)((double)this.c + (double)(f2 - this.c) * 0.01);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBlendFunc((int)0, (int)769);
        GL11.glColor4f((float)this.c, (float)this.c, (float)this.c, (float)1.0f);
        GL11.glBindTexture((int)3553, (int)this.g.n.a("/misc/vignette.png"));
        ho ho2 = ho.a;
        ho2.b();
        ho2.a(0.0, n3, -90.0, 0.0, 1.0);
        ho2.a(n2, n3, -90.0, 1.0, 1.0);
        ho2.a(n2, 0.0, -90.0, 1.0, 0.0);
        ho2.a(0.0, 0.0, -90.0, 0.0, 0.0);
        ho2.a();
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glBlendFunc((int)770, (int)771);
    }

    private void a(int n2, int n3, int n4, float f2) {
        ev ev2 = this.g.g.b.a[n2];
        if (ev2 == null) {
            return;
        }
        float f3 = (float)ev2.b - f2;
        if (f3 > 0.0f) {
            GL11.glPushMatrix();
            float f4 = 1.0f + f3 / 5.0f;
            GL11.glTranslatef((float)(n3 + 8), (float)(n4 + 12), (float)0.0f);
            GL11.glScalef((float)(1.0f / f4), (float)((f4 + 1.0f) / 2.0f), (float)1.0f);
            GL11.glTranslatef((float)(-(n3 + 8)), (float)(-(n4 + 12)), (float)0.0f);
        }
        d.a(this.g.o, this.g.n, ev2, n3, n4);
        if (f3 > 0.0f) {
            GL11.glPopMatrix();
        }
        d.b(this.g.o, this.g.n, ev2, n3, n4);
    }

    public void a() {
        if (this.j > 0) {
            --this.j;
        }
        ++this.h;
        for (int i2 = 0; i2 < this.e.size(); ++i2) {
            ++((ko)this.e.get((int)i2)).b;
        }
    }

    public void a(String string) {
        while (this.g.o.a(string) > 320) {
            int n2;
            for (n2 = 1; n2 < string.length() && this.g.o.a(string.substring(0, n2 + 1)) <= 320; ++n2) {
            }
            this.a(string.substring(0, n2));
            string = string.substring(n2);
        }
        this.e.add(0, new ko(string));
        while (this.e.size() > 50) {
            this.e.remove(this.e.size() - 1);
        }
    }

    public void b(String string) {
        this.i = "Now playing: " + string;
        this.j = 60;
    }
}

