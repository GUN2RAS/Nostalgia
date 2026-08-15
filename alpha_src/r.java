/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class r
extends ex {
    private Map b = new HashMap();

    public void a(bd bd2, double d2, double d3, double d4, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2 + 0.5f), (float)((float)d3), (float)((float)d4 + 0.5f));
        kh kh2 = (kh)this.b.get(bd2.b);
        if (kh2 == null) {
            kh2 = ew.a(bd2.b, null);
            this.b.put(bd2.b, kh2);
        }
        if (kh2 != null) {
            kh2.a(bd2.e);
            float f3 = 0.4375f;
            GL11.glTranslatef((float)0.0f, (float)0.4f, (float)0.0f);
            GL11.glRotatef((float)((float)(bd2.d + (bd2.c - bd2.d) * (double)f2) * 10.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)-30.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslatef((float)0.0f, (float)-0.4f, (float)0.0f);
            GL11.glScalef((float)f3, (float)f3, (float)f3);
            kh2.c(d2, d3, d4, 0.0f, 0.0f);
            kx.a.a(kh2, 0.0, 0.0, 0.0, 0.0f, f2);
        }
        GL11.glPopMatrix();
    }
}

