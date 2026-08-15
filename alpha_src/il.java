/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class il
extends hq {
    public il(Minecraft minecraft) {
        super(minecraft);
        this.b = true;
    }

    public void b(dm dm2) {
        for (int i2 = 0; i2 < 9; ++i2) {
            if (dm2.b.a[i2] == null) {
                this.a.g.b.a[i2] = new ev(((ly)dl.a.get((int)i2)).bc);
                continue;
            }
            this.a.g.b.a[i2].a = 1;
        }
    }

    public boolean d() {
        return false;
    }

    public void a(cn cn2) {
        super.a(cn2);
    }

    public void c() {
    }
}

