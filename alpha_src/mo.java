/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class mo
extends fk {
    public float i = 1.0f;
    public boolean j = false;
    private int l = 0;

    public mo(int n2, int n3, int n4, int n5, String string, float f2) {
        super(n2, n3, n4, 150, 20, string);
        this.l = n5;
        this.i = f2;
    }

    protected int a(boolean bl2) {
        return 0;
    }

    protected void b(Minecraft minecraft, int n2, int n3) {
        if (!this.h) {
            return;
        }
        if (this.j) {
            this.i = (float)(n2 - (this.c + 4)) / (float)(this.a - 8);
            if (this.i < 0.0f) {
                this.i = 0.0f;
            }
            if (this.i > 1.0f) {
                this.i = 1.0f;
            }
            minecraft.y.a(this.l, this.i);
            this.e = minecraft.y.d(this.l);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.b(this.c + (int)(this.i * (float)(this.a - 8)), this.d, 0, 66, 4, 20);
        this.b(this.c + (int)(this.i * (float)(this.a - 8)) + 4, this.d, 196, 66, 4, 20);
    }

    public boolean c(Minecraft minecraft, int n2, int n3) {
        if (super.c(minecraft, n2, n3)) {
            this.i = (float)(n2 - (this.c + 4)) / (float)(this.a - 8);
            if (this.i < 0.0f) {
                this.i = 0.0f;
            }
            if (this.i > 1.0f) {
                this.i = 1.0f;
            }
            minecraft.y.a(this.l, this.i);
            this.e = minecraft.y.d(this.l);
            this.j = true;
            return true;
        }
        return false;
    }

    public void a(int n2, int n3) {
        this.j = false;
    }
}

