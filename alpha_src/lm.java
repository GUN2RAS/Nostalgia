/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class lm {
    protected float k = 0.0f;

    protected void a(int n2, int n3, int n4, int n5, int n6) {
        float f2 = (float)(n6 >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n6 >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n6 >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n6 & 0xFF) / 255.0f;
        ho ho2 = ho.a;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        ho2.b();
        ho2.a((double)n2, (double)n5, 0.0);
        ho2.a((double)n4, (double)n5, 0.0);
        ho2.a((double)n4, (double)n3, 0.0);
        ho2.a((double)n2, (double)n3, 0.0);
        ho2.a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    protected void a(int n2, int n3, int n4, int n5, int n6, int n7) {
        float f2 = (float)(n6 >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n6 >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n6 >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n6 & 0xFF) / 255.0f;
        float f6 = (float)(n7 >> 24 & 0xFF) / 255.0f;
        float f7 = (float)(n7 >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n7 >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n7 & 0xFF) / 255.0f;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3008);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        ho ho2 = ho.a;
        ho2.b();
        ho2.a(f3, f4, f5, f2);
        ho2.a((double)n4, (double)n3, 0.0);
        ho2.a((double)n2, (double)n3, 0.0);
        ho2.a(f7, f8, f9, f6);
        ho2.a((double)n2, (double)n5, 0.0);
        ho2.a((double)n4, (double)n5, 0.0);
        ho2.a();
        GL11.glShadeModel((int)7424);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3553);
    }

    public void a(kd kd2, String string, int n2, int n3, int n4) {
        kd2.a(string, n2 - kd2.a(string) / 2, n3, n4);
    }

    public void b(kd kd2, String string, int n2, int n3, int n4) {
        kd2.a(string, n2, n3, n4);
    }

    public void b(int n2, int n3, int n4, int n5, int n6, int n7) {
        float f2 = 0.00390625f;
        float f3 = 0.00390625f;
        ho ho2 = ho.a;
        ho2.b();
        ho2.a(n2 + 0, n3 + n7, this.k, (float)(n4 + 0) * f2, (float)(n5 + n7) * f3);
        ho2.a(n2 + n6, n3 + n7, this.k, (float)(n4 + n6) * f2, (float)(n5 + n7) * f3);
        ho2.a(n2 + n6, n3 + 0, this.k, (float)(n4 + n6) * f2, (float)(n5 + 0) * f3);
        ho2.a(n2 + 0, n3 + 0, this.k, (float)(n4 + 0) * f2, (float)(n5 + 0) * f3);
        ho2.a();
    }
}

