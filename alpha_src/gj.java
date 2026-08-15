/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class gj
extends ak {
    public void a(kh kh2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glPushMatrix();
        gj.a(kh2.au, d2 - kh2.aI, d3 - kh2.aJ, d4 - kh2.aK);
        GL11.glPopMatrix();
    }
}

