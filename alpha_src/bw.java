/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class bw
extends ak {
    private Random d = new Random();

    public void a(jc jc2, double d2, double d3, double d4, float f2, float f3) {
        this.d.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        GL11.glRotatef((float)f2, (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glEnable((int)32826);
        this.a("/art/kz.png");
        er er2 = jc2.b;
        float f4 = 0.0625f;
        GL11.glScalef((float)f4, (float)f4, (float)f4);
        this.a(jc2, er2.z, er2.A, er2.B, er2.C);
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }

    private void a(jc jc2, int n2, int n3, int n4, int n5) {
        float f2 = (float)(-n2) / 2.0f;
        float f3 = (float)(-n3) / 2.0f;
        float f4 = -0.5f;
        float f5 = 0.5f;
        for (int i2 = 0; i2 < n2 / 16; ++i2) {
            for (int i3 = 0; i3 < n3 / 16; ++i3) {
                float f6 = f2 + (float)((i2 + 1) * 16);
                float f7 = f2 + (float)(i2 * 16);
                float f8 = f3 + (float)((i3 + 1) * 16);
                float f9 = f3 + (float)(i3 * 16);
                this.a(jc2, (f6 + f7) / 2.0f, (f8 + f9) / 2.0f);
                float f10 = (float)(n4 + n2 - i2 * 16) / 256.0f;
                float f11 = (float)(n4 + n2 - (i2 + 1) * 16) / 256.0f;
                float f12 = (float)(n5 + n3 - i3 * 16) / 256.0f;
                float f13 = (float)(n5 + n3 - (i3 + 1) * 16) / 256.0f;
                float f14 = 0.75f;
                float f15 = 0.8125f;
                float f16 = 0.0f;
                float f17 = 0.0625f;
                float f18 = 0.75f;
                float f19 = 0.8125f;
                float f20 = 0.001953125f;
                float f21 = 0.001953125f;
                float f22 = 0.7519531f;
                float f23 = 0.7519531f;
                float f24 = 0.0f;
                float f25 = 0.0625f;
                ho ho2 = ho.a;
                ho2.b();
                ho2.b(0.0f, 0.0f, -1.0f);
                ho2.a(f6, f9, f4, f11, f12);
                ho2.a(f7, f9, f4, f10, f12);
                ho2.a(f7, f8, f4, f10, f13);
                ho2.a(f6, f8, f4, f11, f13);
                ho2.b(0.0f, 0.0f, 1.0f);
                ho2.a(f6, f8, f5, f14, f16);
                ho2.a(f7, f8, f5, f15, f16);
                ho2.a(f7, f9, f5, f15, f17);
                ho2.a(f6, f9, f5, f14, f17);
                ho2.b(0.0f, -1.0f, 0.0f);
                ho2.a(f6, f8, f4, f18, f20);
                ho2.a(f7, f8, f4, f19, f20);
                ho2.a(f7, f8, f5, f19, f21);
                ho2.a(f6, f8, f5, f18, f21);
                ho2.b(0.0f, 1.0f, 0.0f);
                ho2.a(f6, f9, f5, f18, f20);
                ho2.a(f7, f9, f5, f19, f20);
                ho2.a(f7, f9, f4, f19, f21);
                ho2.a(f6, f9, f4, f18, f21);
                ho2.b(-1.0f, 0.0f, 0.0f);
                ho2.a(f6, f8, f5, f23, f24);
                ho2.a(f6, f9, f5, f23, f25);
                ho2.a(f6, f9, f4, f22, f25);
                ho2.a(f6, f8, f4, f22, f24);
                ho2.b(1.0f, 0.0f, 0.0f);
                ho2.a(f7, f8, f4, f23, f24);
                ho2.a(f7, f9, f4, f23, f25);
                ho2.a(f7, f9, f5, f22, f25);
                ho2.a(f7, f8, f5, f22, f24);
                ho2.a();
            }
        }
    }

    private void a(jc jc2, float f2, float f3) {
        int n2 = eo.b(jc2.ak);
        int n3 = eo.b(jc2.al + (double)(f3 / 16.0f));
        int n4 = eo.b(jc2.am);
        if (jc2.a == 0) {
            n2 = eo.b(jc2.ak + (double)(f2 / 16.0f));
        }
        if (jc2.a == 1) {
            n4 = eo.b(jc2.am - (double)(f2 / 16.0f));
        }
        if (jc2.a == 2) {
            n2 = eo.b(jc2.ak - (double)(f2 / 16.0f));
        }
        if (jc2.a == 3) {
            n4 = eo.b(jc2.am + (double)(f2 / 16.0f));
        }
        float f4 = this.a.g.c(n2, n3, n4);
        GL11.glColor3f((float)f4, (float)f4, (float)f4);
    }
}

