/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class bi
extends dm {
    public lv a;
    private Minecraft bg;

    public bi(Minecraft minecraft, cn cn2, dl dl2) {
        super(cn2);
        this.bg = minecraft;
        if (dl2 != null && dl2.b != null && dl2.b.length() > 0) {
            this.aY = "http://www.minecraft.net/skin/" + dl2.b + ".png";
            System.out.println("Loading texture " + this.aY);
        }
        this.i = dl2.b;
    }

    public void b_() {
        super.b_();
        this.V = this.a.a;
        this.W = this.a.b;
        this.Y = this.a.d;
    }

    public void j() {
        this.a.a(this);
        if (this.a.e && this.aL < 0.2f) {
            this.aL = 0.2f;
        }
        super.j();
    }

    public void k() {
        this.a.a();
    }

    public void a(int n2, boolean bl2) {
        this.a.a(n2, bl2);
    }

    public void a(hm hm2) {
        super.a(hm2);
        hm2.a("Score", this.d);
    }

    public void b(hm hm2) {
        super.b(hm2);
        this.d = hm2.e("Score");
    }

    public void a(gh gh2) {
        this.bg.a(new ea(this.b, gh2));
    }

    public void a(ob ob2) {
        this.bg.a(new nv(ob2));
    }

    public void l() {
        this.bg.a(new hx(this.b));
    }

    public void a(ke ke2) {
        this.bg.a(new id(this.b, ke2));
    }

    public void a(kh kh2) {
        int n2 = this.b.a(kh2);
        if (n2 > 0) {
            kh2.a(this, n2);
            ev ev2 = this.t();
            if (ev2 != null && kh2 instanceof ge) {
                ev2.a((ge)kh2);
                if (ev2.a <= 0) {
                    ev2.a(this);
                    this.u();
                }
            }
        }
    }

    public void a_(kh kh2, int n2) {
        this.bg.h.a(new cd(this.bg.e, kh2, this, -0.5f));
    }

    public int m() {
        return this.b.f();
    }

    public void a_(kh kh2) {
        if (kh2.a(this)) {
            return;
        }
        ev ev2 = this.t();
        if (ev2 != null && kh2 instanceof ge) {
            ev2.b((ge)kh2);
            if (ev2.a <= 0) {
                ev2.a(this);
                this.u();
            }
        }
    }

    public void a(String string) {
    }

    public void n() {
    }

    public boolean o() {
        return this.a.e;
    }
}

