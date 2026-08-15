/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class cp
extends ak {
    protected fo d;

    public cp() {
        this.b = 0.5f;
        this.d = new cl();
    }

    public void a(dc dc2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        GL11.glRotatef((float)(180.0f - f2), (float)0.0f, (float)1.0f, (float)0.0f);
        float f4 = (float)dc2.b - f3;
        float f5 = (float)dc2.a - f3;
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f4 > 0.0f) {
            GL11.glRotatef((float)(eo.a(f4) * f4 * f5 / 10.0f * (float)dc2.c), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        this.a("/terrain.png");
        float f6 = 0.75f;
        GL11.glScalef((float)f6, (float)f6, (float)f6);
        GL11.glScalef((float)(1.0f / f6), (float)(1.0f / f6), (float)(1.0f / f6));
        this.a("/item/boat.png");
        GL11.glScalef((float)-1.0f, (float)-1.0f, (float)1.0f);
        this.d.b(0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }
}

