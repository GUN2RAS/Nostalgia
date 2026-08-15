/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class ei
extends ak {
    public void a(ao ao2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        GL11.glEnable((int)32826);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        int n2 = di.aB.a((ev)null);
        this.a("/gui/items.png");
        ho ho2 = ho.a;
        float f4 = (float)(n2 % 16 * 16 + 0) / 256.0f;
        float f5 = (float)(n2 % 16 * 16 + 16) / 256.0f;
        float f6 = (float)(n2 / 16 * 16 + 0) / 256.0f;
        float f7 = (float)(n2 / 16 * 16 + 16) / 256.0f;
        float f8 = 1.0f;
        float f9 = 0.5f;
        float f10 = 0.25f;
        GL11.glRotatef((float)(180.0f - this.a.i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-this.a.j), (float)1.0f, (float)0.0f, (float)0.0f);
        ho2.b();
        ho2.b(0.0f, 1.0f, 0.0f);
        ho2.a(0.0f - f9, 0.0f - f10, 0.0, f4, f7);
        ho2.a(f8 - f9, 0.0f - f10, 0.0, f5, f7);
        ho2.a(f8 - f9, 1.0f - f10, 0.0, f5, f6);
        ho2.a(0.0f - f9, 1.0f - f10, 0.0, f4, f6);
        ho2.a();
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }
}

