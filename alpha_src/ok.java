/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class ok
extends dn {
    public ok() {
        super(new jy(), 1.0f);
        this.a(new jy());
    }

    protected float a(ax ax2) {
        return 180.0f;
    }

    protected boolean a(ax ax2, int n2) {
        if (n2 != 0) {
            return false;
        }
        if (n2 != 0) {
            return false;
        }
        this.a("/mob/spider_eyes.png");
        float f2 = (1.0f - ax2.a(1.0f)) * 0.5f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3008);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)f2);
        return true;
    }
}

