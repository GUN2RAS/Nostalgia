/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class nz
extends dn {
    private float f;

    public nz(fo fo2, float f2, float f3) {
        super(fo2, f2 * f3);
        this.f = f3;
    }

    protected void a(hl hl2, float f2) {
        GL11.glScalef((float)this.f, (float)this.f, (float)this.f);
    }
}

