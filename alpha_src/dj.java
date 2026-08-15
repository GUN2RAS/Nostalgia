/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class dj
extends ak {
    private bc d = new bc();

    public dj() {
        this.b = 0.5f;
    }

    public void a(ff ff2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        this.a("/terrain.png");
        ly ly2 = ly.n[ff2.a];
        cn cn2 = ff2.i();
        GL11.glDisable((int)2896);
        this.d.a(ly2, cn2, eo.b(ff2.ak), eo.b(ff2.al), eo.b(ff2.am));
        GL11.glEnable((int)2896);
        GL11.glPopMatrix();
    }
}

