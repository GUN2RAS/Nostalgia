/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class kt
extends ak {
    protected fo d;

    public kt() {
        this.b = 0.5f;
        this.d = new hj();
    }

    public void a(oc oc2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glPushMatrix();
        double d5 = oc2.aI + (oc2.ak - oc2.aI) * (double)f3;
        double d6 = oc2.aJ + (oc2.al - oc2.aJ) * (double)f3;
        double d7 = oc2.aK + (oc2.am - oc2.aK) * (double)f3;
        double d8 = 0.3f;
        aj aj2 = oc2.g(d5, d6, d7);
        float f4 = oc2.at + (oc2.ar - oc2.at) * f3;
        if (aj2 != null) {
            aj aj3 = oc2.a(d5, d6, d7, d8);
            aj aj4 = oc2.a(d5, d6, d7, -d8);
            if (aj3 == null) {
                aj3 = aj2;
            }
            if (aj4 == null) {
                aj4 = aj2;
            }
            d2 += aj2.a - d5;
            d3 += (aj3.b + aj4.b) / 2.0 - d6;
            d4 += aj2.c - d7;
            aj aj5 = aj4.c(-aj3.a, -aj3.b, -aj3.c);
            if (aj5.c() != 0.0) {
                aj5 = aj5.b();
                f2 = (float)(Math.atan2(aj5.c, aj5.a) * 180.0 / Math.PI);
                f4 = (float)(Math.atan(aj5.b) * 73.0);
            }
        }
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        GL11.glRotatef((float)(180.0f - f2), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-f4), (float)0.0f, (float)0.0f, (float)1.0f);
        float f5 = (float)oc2.b - f3;
        float f6 = (float)oc2.a - f3;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 0.0f) {
            GL11.glRotatef((float)(eo.a(f5) * f5 * f6 / 10.0f * (float)oc2.c), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (oc2.d != 0) {
            this.a("/terrain.png");
            float f7 = 0.75f;
            GL11.glScalef((float)f7, (float)f7, (float)f7);
            GL11.glTranslatef((float)0.0f, (float)0.3125f, (float)0.0f);
            GL11.glRotatef((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            if (oc2.d == 1) {
                new bc().a(ly.av);
            } else if (oc2.d == 2) {
                new bc().a(ly.aC);
            }
            GL11.glRotatef((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslatef((float)0.0f, (float)-0.3125f, (float)0.0f);
            GL11.glScalef((float)(1.0f / f7), (float)(1.0f / f7), (float)(1.0f / f7));
        }
        this.a("/item/cart.png");
        GL11.glScalef((float)-1.0f, (float)-1.0f, (float)1.0f);
        this.d.b(0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }
}

