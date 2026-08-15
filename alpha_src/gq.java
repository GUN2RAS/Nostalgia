/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class gq
extends dn {
    private fo f;

    public gq(fo fo2, fo fo3, float f2) {
        super(fo2, f2);
        this.f = fo3;
    }

    protected boolean a(ma ma2, int n2) {
        if (n2 == 0) {
            this.a(this.f);
            GL11.glEnable((int)2977);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            return true;
        }
        if (n2 == 1) {
            GL11.glDisable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        return false;
    }

    protected void a(ma ma2, float f2) {
        float f3 = (ma2.b + (ma2.a - ma2.b) * f2) / ((float)ma2.c * 0.5f + 1.0f);
        float f4 = 1.0f / (f3 + 1.0f);
        float f5 = ma2.c;
        GL11.glScalef((float)(f4 * f5), (float)(1.0f / f4 * f5), (float)(f4 * f5));
    }
}

