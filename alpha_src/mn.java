/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class mn
extends bh {
    private gy a;
    private boolean h = false;

    public mn(Minecraft minecraft, String string, int n2) {
        minecraft.a((cn)null);
        new nc(this, minecraft, string, n2).start();
    }

    public void g() {
        if (this.a != null) {
            this.a.a();
        }
    }

    protected void a(char c2, int n2) {
    }

    public void a() {
        this.e.clear();
        this.e.add(new fk(0, this.c / 2 - 100, this.d / 4 + 120 + 12, "Cancel"));
    }

    protected void a(fk fk2) {
        if (fk2.f == 0) {
            this.h = true;
            if (this.a != null) {
                this.a.b();
            }
            this.b.a(new cx());
        }
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        if (this.a == null) {
            this.a(this.g, "Connecting to the server...", this.c / 2, this.d / 2 - 50, 0xFFFFFF);
            this.a(this.g, "", this.c / 2, this.d / 2 - 10, 0xFFFFFF);
        } else {
            this.a(this.g, "Logging in...", this.c / 2, this.d / 2 - 50, 0xFFFFFF);
            this.a(this.g, this.a.a, this.c / 2, this.d / 2 - 10, 0xFFFFFF);
        }
        super.a(n2, n3, f2);
    }

    static /* synthetic */ gy a(mn mn2, gy gy2) {
        mn2.a = gy2;
        return mn2.a;
    }

    static /* synthetic */ boolean a(mn mn2) {
        return mn2.h;
    }

    static /* synthetic */ gy b(mn mn2) {
        return mn2.a;
    }
}

