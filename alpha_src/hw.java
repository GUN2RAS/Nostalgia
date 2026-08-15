/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class hw
extends ak {
    private bc d = new bc();

    public hw() {
        this.b = 0.5f;
    }

    public void a(jd jd2, double d2, double d3, double d4, float f2, float f3) {
        float f4;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        if ((float)jd2.a - f3 + 1.0f < 10.0f) {
            f4 = 1.0f - ((float)jd2.a - f3 + 1.0f) / 10.0f;
            if (f4 < 0.0f) {
                f4 = 0.0f;
            }
            if (f4 > 1.0f) {
                f4 = 1.0f;
            }
            f4 *= f4;
            f4 *= f4;
            float f5 = 1.0f + f4 * 0.3f;
            GL11.glScalef((float)f5, (float)f5, (float)f5);
        }
        f4 = (1.0f - ((float)jd2.a - f3 + 1.0f) / 100.0f) * 0.8f;
        this.a("/terrain.png");
        this.d.a(ly.an);
        if (jd2.a / 5 % 2 == 0) {
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)772);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)f4);
            this.d.a(ly.an);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
        }
        GL11.glPopMatrix();
    }
}

