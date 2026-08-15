/*
 * Decompiled with CFR 0.152.
 */
import java.io.File;
import net.minecraft.client.Minecraft;

public class jq
extends bh {
    protected bh a;
    protected String h = "Select world";
    private boolean i = false;

    public jq(bh bh2) {
        this.a = bh2;
    }

    public void a() {
        File file = Minecraft.b();
        for (int i2 = 0; i2 < 5; ++i2) {
            hm hm2 = cn.a(file, "World" + (i2 + 1));
            if (hm2 == null) {
                this.e.add(new fk(i2, this.c / 2 - 100, this.d / 6 + 24 * i2, "- empty -"));
                continue;
            }
            String string = "World " + (i2 + 1);
            long l2 = hm2.f("SizeOnDisk");
            string = string + " (" + (float)(l2 / 1024L * 100L / 1024L) / 100.0f + " MB)";
            this.e.add(new fk(i2, this.c / 2 - 100, this.d / 6 + 24 * i2, string));
        }
        this.j();
    }

    protected String d(int n2) {
        File file = Minecraft.b();
        return cn.a(file, "World" + n2) != null ? "World" + n2 : null;
    }

    public void j() {
        this.e.add(new fk(5, this.c / 2 - 100, this.d / 6 + 120 + 12, "Delete world..."));
        this.e.add(new fk(6, this.c / 2 - 100, this.d / 6 + 168, "Cancel"));
    }

    protected void a(fk fk2) {
        if (!fk2.g) {
            return;
        }
        if (fk2.f < 5) {
            this.c(fk2.f + 1);
        } else if (fk2.f == 5) {
            this.b.a(new ib(this));
        } else if (fk2.f == 6) {
            this.b.a(this.a);
        }
    }

    public void c(int n2) {
        this.b.a((bh)null);
        if (this.i) {
            return;
        }
        this.i = true;
        this.b.b = new ia(this.b);
        this.b.b("World" + n2);
        this.b.a((bh)null);
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, this.h, this.c / 2, 20, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

