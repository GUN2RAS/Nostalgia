/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class hx
extends ee {
    public et j = new et();

    public hx(eu eu2) {
        int n2;
        int n3;
        this.i.add(new an(this, this.j.a, this.j.b, 0, 124, 35));
        for (n3 = 0; n3 < 3; ++n3) {
            for (n2 = 0; n2 < 3; ++n2) {
                this.i.add(new mm(this, this.j.a, n2 + n3 * 3, 30 + n2 * 18, 17 + n3 * 18));
            }
        }
        for (n3 = 0; n3 < 3; ++n3) {
            for (n2 = 0; n2 < 9; ++n2) {
                this.i.add(new mm(this, eu2, n2 + (n3 + 1) * 9, 8 + n2 * 18, 84 + n3 * 18));
            }
        }
        for (n3 = 0; n3 < 9; ++n3) {
            this.i.add(new mm(this, eu2, n3, 8 + n3 * 18, 142));
        }
    }

    public void h() {
        super.h();
        this.j.a(this.b.g);
    }

    protected void j() {
        this.g.b("Crafting", 28, 6, 0x404040);
        this.g.b("Inventory", 8, this.h - 96 + 2, 0x404040);
    }

    protected void a(float f2) {
        int n2 = this.b.n.a("/gui/crafting.png");
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.b.n.b(n2);
        int n3 = (this.c - this.a) / 2;
        int n4 = (this.d - this.h) / 2;
        this.b(n3, n4, 0, 0, this.a, this.h);
    }
}

