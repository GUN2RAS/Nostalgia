/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class hq {
    protected final Minecraft a;
    public boolean b = false;

    public hq(Minecraft minecraft) {
        this.a = minecraft;
    }

    public void a(cn cn2) {
    }

    public void a(int n2, int n3, int n4, int n5) {
        this.b(n2, n3, n4, n5);
    }

    public boolean b(int n2, int n3, int n4, int n5) {
        this.a.h.a(n2, n3, n4);
        cn cn2 = this.a.e;
        ly ly2 = ly.n[cn2.a(n2, n3, n4)];
        int n6 = cn2.e(n2, n3, n4);
        boolean bl2 = cn2.d(n2, n3, n4, 0);
        if (ly2 != null && bl2) {
            this.a.A.b(ly2.bl.a(), (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, (ly2.bl.b() + 1.0f) / 2.0f, ly2.bl.c() * 0.8f);
            ly2.b(cn2, n2, n3, n4, n6);
        }
        return bl2;
    }

    public void c(int n2, int n3, int n4, int n5) {
    }

    public void a() {
    }

    public void a(float f2) {
    }

    public float b() {
        return 5.0f;
    }

    public void a(dm dm2) {
    }

    public void c() {
    }

    public boolean d() {
        return true;
    }

    public void b(dm dm2) {
    }

    public boolean a(dm dm2, cn cn2, ev ev2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.a(n2, n3, n4);
        if (n6 > 0 && ly.n[n6].a(cn2, n2, n3, n4, dm2)) {
            return true;
        }
        if (ev2 == null) {
            return false;
        }
        return ev2.a(dm2, cn2, n2, n3, n4, n5);
    }

    public dm b(cn cn2) {
        return new bi(this.a, cn2, this.a.i);
    }
}

