/*
 * Decompiled with CFR 0.152.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class gy
extends lb {
    private boolean c = false;
    private ii d;
    public String a;
    private Minecraft e;
    private gs f;
    private boolean g = false;
    Random b = new Random();

    public gy(Minecraft minecraft, String string, int n2) {
        this.e = minecraft;
        Socket socket = new Socket(InetAddress.getByName(string), n2);
        this.d = new ii(socket, "Client", this);
    }

    public void a() {
        if (this.c) {
            return;
        }
        this.d.a();
    }

    public void a(hp hp2) {
        this.e.b = new nj(this.e, this);
        this.f = new gs(this);
        this.f.y = true;
        this.e.a(this.f);
        this.e.a(new dg(this));
    }

    public void a(ha ha2) {
        double d2 = (double)ha2.b / 32.0;
        double d3 = (double)ha2.c / 32.0;
        double d4 = (double)ha2.d / 32.0;
        dx dx2 = new dx(this.f, d2, d3, d4, new ev(ha2.h, ha2.i));
        dx2.an = (double)ha2.e / 128.0;
        dx2.ao = (double)ha2.f / 128.0;
        dx2.ap = (double)ha2.g / 128.0;
        dx2.bd = ha2.b;
        dx2.be = ha2.c;
        dx2.bf = ha2.d;
        this.f.a(ha2.a, dx2);
    }

    public void a(kj kj2) {
        double d2 = (double)kj2.b / 32.0;
        double d3 = (double)kj2.c / 32.0;
        double d4 = (double)kj2.d / 32.0;
        kh kh2 = null;
        if (kj2.e == 10) {
            kh2 = new oc(this.f, d2, d3, d4, 0);
        }
        if (kj2.e == 11) {
            kh2 = new oc(this.f, d2, d3, d4, 1);
        }
        if (kj2.e == 12) {
            kh2 = new oc(this.f, d2, d3, d4, 2);
        }
        if (kj2.e == 1) {
            kh2 = new dc(this.f, d2, d3, d4);
        }
        if (kh2 != null) {
            kh2.bd = kj2.b;
            kh2.be = kj2.c;
            kh2.bf = kj2.d;
            kh2.aq = 0.0f;
            kh2.ar = 0.0f;
            kh2.ab = kj2.a;
            this.f.a(kj2.a, kh2);
        }
    }

    public void a(gp gp2) {
        double d2 = (double)gp2.c / 32.0;
        double d3 = (double)gp2.d / 32.0;
        double d4 = (double)gp2.e / 32.0;
        float f2 = (float)(gp2.f * 360) / 256.0f;
        float f3 = (float)(gp2.g * 360) / 256.0f;
        nt nt2 = new nt(this.e.e, gp2.b);
        nt2.bd = gp2.c;
        nt2.be = gp2.d;
        nt2.bf = gp2.e;
        int n2 = gp2.h;
        nt2.b.a[nt2.b.d] = n2 == 0 ? null : new ev(n2);
        nt2.b(d2, d3, d4, f2, f3);
        this.f.a(gp2.a, nt2);
    }

    public void a(jl jl2) {
        kh kh2 = this.f.b(jl2.a);
        if (kh2 == null) {
            return;
        }
        kh2.bd = jl2.b;
        kh2.be = jl2.c;
        kh2.bf = jl2.d;
        double d2 = (double)kh2.bd / 32.0;
        double d3 = (double)kh2.be / 32.0;
        double d4 = (double)kh2.bf / 32.0;
        float f2 = (float)(jl2.e * 360) / 256.0f;
        float f3 = (float)(jl2.f * 360) / 256.0f;
        kh2.a(d2, d3, d4, f2, f3, 3);
    }

    public void a(lq lq2) {
        kh kh2 = this.f.b(lq2.a);
        if (kh2 == null) {
            return;
        }
        kh2.bd += lq2.b;
        kh2.be += lq2.c;
        kh2.bf += lq2.d;
        double d2 = (double)kh2.bd / 32.0;
        double d3 = (double)kh2.be / 32.0;
        double d4 = (double)kh2.bf / 32.0;
        float f2 = lq2.g ? (float)(lq2.e * 360) / 256.0f : kh2.aq;
        float f3 = lq2.g ? (float)(lq2.f * 360) / 256.0f : kh2.ar;
        kh2.a(d2, d3, d4, f2, f3, 3);
    }

    public void a(ju ju2) {
        this.f.c(ju2.a);
    }

    public void a(eh eh2) {
        bi bi2 = this.e.g;
        double d2 = bi2.ak;
        double d3 = bi2.al;
        double d4 = bi2.am;
        float f2 = bi2.aq;
        float f3 = bi2.ar;
        if (eh2.h) {
            d2 = eh2.a;
            d3 = eh2.b;
            d4 = eh2.c;
        }
        if (eh2.i) {
            f2 = eh2.e;
            f3 = eh2.f;
        }
        bi2.aL = 0.0f;
        bi2.ap = 0.0;
        bi2.ao = 0.0;
        bi2.an = 0.0;
        bi2.b(d2, d3, d4, f2, f3);
        eh2.a = bi2.ak;
        eh2.b = bi2.au.b;
        eh2.c = bi2.am;
        eh2.d = bi2.al;
        this.d.a(eh2);
        if (!this.g) {
            this.e.g.ah = this.e.g.ak;
            this.e.g.ai = this.e.g.al;
            this.e.g.aj = this.e.g.am;
            this.g = true;
            this.e.a((bh)null);
        }
    }

    public void a(ka ka2) {
        this.f.a(ka2.a, ka2.b, ka2.c);
    }

    public void a(na na2) {
        ga ga2 = this.f.b(na2.a, na2.b);
        int n2 = na2.a * 16;
        int n3 = na2.b * 16;
        for (int i2 = 0; i2 < na2.f; ++i2) {
            short s2 = na2.c[i2];
            int n4 = na2.d[i2] & 0xFF;
            byte by2 = na2.e[i2];
            int n5 = s2 >> 12 & 0xF;
            int n6 = s2 >> 8 & 0xF;
            int n7 = s2 & 0xFF;
            ga2.a(n5, n7, n6, n4, (int)by2);
            this.f.c(n5 + n2, n7, n6 + n3, n5 + n2, n7, n6 + n3);
            this.f.b(n5 + n2, n7, n6 + n3, n5 + n2, n7, n6 + n3);
        }
    }

    public void a(bz bz2) {
        this.f.c(bz2.a, bz2.b, bz2.c, bz2.a + bz2.d - 1, bz2.b + bz2.e - 1, bz2.c + bz2.f - 1);
        this.f.a(bz2.a, bz2.b, bz2.c, bz2.d, bz2.e, bz2.f, bz2.g);
    }

    public void a(li li2) {
        this.f.c(li2.a, li2.b, li2.c, li2.d, li2.e);
    }

    public void a(oh oh2) {
        this.d.a("Got kicked");
        this.c = true;
        this.e.a((cn)null);
        this.e.a(new cj("Disconnected by server", oh2.a));
    }

    public void a(String string) {
        if (this.c) {
            return;
        }
        this.c = true;
        this.e.a((cn)null);
        this.e.a(new cj("Connection lost", string));
    }

    public void a(fn fn2) {
        if (this.c) {
            return;
        }
        this.d.a(fn2);
    }

    public void a(bm bm2) {
        dx dx2 = (dx)this.f.b(bm2.a);
        ge ge2 = (ge)this.f.b(bm2.b);
        if (ge2 == null) {
            ge2 = this.e.g;
        }
        if (dx2 != null) {
            this.f.a(dx2, "random.pop", 0.2f, ((this.b.nextFloat() - this.b.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            this.e.h.a(new cd(this.e.e, dx2, ge2, -0.5f));
            this.f.c(bm2.a);
        }
    }

    public void a(dz dz2) {
        kh kh2 = this.f.b(dz2.a);
        if (kh2 == null) {
            return;
        }
        dm dm2 = (dm)kh2;
        int n2 = dz2.b;
        dm2.b.a[dm2.b.d] = n2 == 0 ? null : new ev(n2);
    }

    public void a(ij ij2) {
        this.e.u.a(ij2.a);
    }

    public void a(hf hf2) {
        kh kh2 = this.f.b(hf2.a);
        if (kh2 == null) {
            return;
        }
        dm dm2 = (dm)kh2;
        dm2.w();
    }

    public void a(ld ld2) {
        this.e.g.b.a(new ev(ld2.a, ld2.b, ld2.c));
    }

    public void a(gt gt2) {
        if (gt2.a.equals("-")) {
            this.a((fn)new hp(this.e.i.b, "Password", 2));
        } else {
            try {
                URL uRL = new URL("http://www.minecraft.net/game/joinserver.jsp?user=" + this.e.i.b + "&sessionId=" + this.e.i.c + "&serverId=" + gt2.a);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
                String string = bufferedReader.readLine();
                bufferedReader.close();
                if (string.equalsIgnoreCase("ok")) {
                    this.a((fn)new hp(this.e.i.b, "Password", 2));
                } else {
                    this.d.a("Failed to login: " + string);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                this.d.a("Internal client error: " + exception.toString());
            }
        }
    }

    public void b() {
        this.c = true;
        this.d.a("Closed");
    }

    public void a(ez ez2) {
        double d2 = (double)ez2.c / 32.0;
        double d3 = (double)ez2.d / 32.0;
        double d4 = (double)ez2.e / 32.0;
        float f2 = (float)(ez2.f * 360) / 256.0f;
        float f3 = (float)(ez2.g * 360) / 256.0f;
        ge ge2 = (ge)ew.a(ez2.b, this.e.e);
        ge2.bd = ez2.c;
        ge2.be = ez2.d;
        ge2.bf = ez2.e;
        ge2.b(d2, d3, d4, f2, f3);
        ge2.B = true;
        this.f.a(ez2.a, ge2);
    }

    public void a(du du2) {
        this.e.e.a(du2.a);
    }

    public void a(m m2) {
        bi bi2 = this.e.g;
        if (m2.a == -1) {
            bi2.b.a = m2.b;
        }
        if (m2.a == -2) {
            bi2.b.c = m2.b;
        }
        if (m2.a == -3) {
            bi2.b.b = m2.b;
        }
    }

    public void a(ny ny2) {
        ic ic2 = this.f.b(ny2.a, ny2.b, ny2.c);
        if (ic2 != null) {
            ic2.a(ny2.e);
            this.f.b(ny2.a, ny2.b, ny2.c, ny2.a, ny2.b, ny2.c);
        }
    }

    public void a(ji ji2) {
        this.f.o = ji2.a;
        this.f.p = ji2.b;
        this.f.q = ji2.c;
    }
}

