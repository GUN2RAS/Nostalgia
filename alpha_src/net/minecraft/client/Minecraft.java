/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.input.Controllers
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.DisplayMode
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import net.minecraft.client.MinecraftApplet;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public abstract class Minecraft
implements Runnable {
    public hq b;
    private boolean a = false;
    public int c;
    public int d;
    private be L;
    private ir M = new ir(20.0f);
    public cn e;
    public e f;
    public bi g;
    public bq h;
    public dl i = null;
    public String j;
    public Canvas k;
    public boolean l = true;
    public volatile boolean m = false;
    public ey n;
    public kd o;
    public bh p = null;
    public gr q = new gr(this);
    public iq r = new iq(this);
    private bf N;
    private int O = 0;
    private int P = 0;
    private int Q;
    private int R;
    public String s = null;
    public int t = 0;
    public lu u;
    public boolean v = false;
    public cr w = new cr(0.0f);
    public mf x = null;
    public fr y;
    protected MinecraftApplet z;
    public of A = new of();
    public mp B;
    public File C;
    public static long[] D = new long[512];
    public static int E = 0;
    private String S;
    private int T;
    private ml U = new ml();
    private at V = new at();
    private static File W = null;
    public volatile boolean F = true;
    public String G = "";
    long H = -1L;
    public boolean I = false;
    private int X = 0;
    public boolean J = false;
    long K = System.currentTimeMillis();
    private int Y = 0;

    public Minecraft(Component component, Canvas canvas, MinecraftApplet minecraftApplet, int n2, int n3, boolean bl2) {
        this.Q = n2;
        this.R = n3;
        this.a = bl2;
        this.z = minecraftApplet;
        new fl(this, "Timer hack thread");
        this.k = canvas;
        this.c = n2;
        this.d = n3;
        this.a = bl2;
    }

    public abstract void a(go var1);

    public void a(String string, int n2) {
        this.S = string;
        this.T = n2;
    }

    public void a() {
        if (this.k != null) {
            Graphics graphics = this.k.getGraphics();
            if (graphics != null) {
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, this.c, this.d);
                graphics.dispose();
            }
            Display.setParent((Canvas)this.k);
        } else if (this.a) {
            Display.setFullscreen((boolean)true);
            this.c = Display.getDisplayMode().getWidth();
            this.d = Display.getDisplayMode().getHeight();
            if (this.c <= 0) {
                this.c = 1;
            }
            if (this.d <= 0) {
                this.d = 1;
            }
        } else {
            Display.setDisplayMode((DisplayMode)new DisplayMode(this.c, this.d));
        }
        Display.setTitle((String)"Minecraft Minecraft Alpha v1.1.2_01");
        try {
            Display.create();
        }
        catch (LWJGLException lWJGLException) {
            lWJGLException.printStackTrace();
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            Display.create();
        }
        kx.a.f = new jh(this);
        this.C = Minecraft.b();
        this.y = new fr(this, this.C);
        this.n = new ey(this.y);
        this.o = new kd(this.y, "/default.png", this.n);
        this.p();
        Keyboard.create();
        Mouse.create();
        this.B = new mp(this.k);
        try {
            Controllers.create();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.c("Pre startup");
        GL11.glEnable((int)3553);
        GL11.glShadeModel((int)7425);
        GL11.glClearDepth((double)1.0);
        GL11.glEnable((int)2929);
        GL11.glDepthFunc((int)515);
        GL11.glEnable((int)3008);
        GL11.glAlphaFunc((int)516, (float)0.1f);
        GL11.glCullFace((int)1029);
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode((int)5888);
        this.c("Startup");
        this.L = new be();
        this.A.a(this.y);
        this.n.a(this.V);
        this.n.a(this.U);
        this.n.a(new aa(this));
        this.n.a(new ht());
        this.n.a(new eg());
        this.n.a(new jz(0));
        this.n.a(new jz(1));
        this.f = new e(this, this.n);
        GL11.glViewport((int)0, (int)0, (int)this.c, (int)this.d);
        this.h = new bq(this.e, this.n);
        try {
            this.N = new bf(this.C, this);
            this.N.start();
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.c("Post startup");
        this.u = new lu(this);
        if (this.S != null) {
            this.a(new mn(this, this.S, this.T));
        } else {
            this.a(new cx());
        }
    }

    private void p() {
        iy iy2 = new iy(this.c, this.d);
        int n2 = iy2.a();
        int n3 = iy2.b();
        GL11.glClear((int)16640);
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        GL11.glOrtho((double)0.0, (double)n2, (double)n3, (double)0.0, (double)1000.0, (double)3000.0);
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-2000.0f);
        GL11.glViewport((int)0, (int)0, (int)this.c, (int)this.d);
        GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        ho ho2 = ho.a;
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2912);
        GL11.glBindTexture((int)3553, (int)this.n.a("/title/mojang.png"));
        ho2.b();
        ho2.b(0xFFFFFF);
        ho2.a(0.0, this.d, 0.0, 0.0, 0.0);
        ho2.a(this.c, this.d, 0.0, 0.0, 0.0);
        ho2.a(this.c, 0.0, 0.0, 0.0, 0.0);
        ho2.a(0.0, 0.0, 0.0, 0.0, 0.0);
        ho2.a();
        int n4 = 256;
        int n5 = 256;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ho2.b(0xFFFFFF);
        this.a((this.c / 2 - n4) / 2, (this.d / 2 - n5) / 2, 0, 0, n4, n5);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2912);
        GL11.glEnable((int)3008);
        GL11.glAlphaFunc((int)516, (float)0.1f);
        Display.swapBuffers();
    }

    public void a(int n2, int n3, int n4, int n5, int n6, int n7) {
        float f2 = 0.00390625f;
        float f3 = 0.00390625f;
        ho ho2 = ho.a;
        ho2.b();
        ho2.a(n2 + 0, n3 + n7, 0.0, (float)(n4 + 0) * f2, (float)(n5 + n7) * f3);
        ho2.a(n2 + n6, n3 + n7, 0.0, (float)(n4 + n6) * f2, (float)(n5 + n7) * f3);
        ho2.a(n2 + n6, n3 + 0, 0.0, (float)(n4 + n6) * f2, (float)(n5 + 0) * f3);
        ho2.a(n2 + 0, n3 + 0, 0.0, (float)(n4 + 0) * f2, (float)(n5 + 0) * f3);
        ho2.a();
    }

    public static File b() {
        if (W == null) {
            W = Minecraft.a("minecraft");
        }
        return W;
    }

    public static File a(String string) {
        File file;
        String string2 = System.getProperty("user.home", ".");
        switch (Minecraft.q()) {
            case a: 
            case b: {
                file = new File(string2, '.' + string + '/');
                break;
            }
            case c: {
                String string3 = System.getenv("APPDATA");
                if (string3 != null) {
                    file = new File(string3, "." + string + '/');
                    break;
                }
                file = new File(string2, '.' + string + '/');
                break;
            }
            case d: {
                file = new File(string2, "Library/Application Support/" + string);
                break;
            }
            default: {
                file = new File(string2, string + '/');
            }
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + file);
        }
        return file;
    }

    private static ih q() {
        String string = System.getProperty("os.name").toLowerCase();
        if (string.contains("win")) {
            return ih.c;
        }
        if (string.contains("mac")) {
            return ih.d;
        }
        if (string.contains("solaris")) {
            return ih.b;
        }
        if (string.contains("sunos")) {
            return ih.b;
        }
        if (string.contains("linux")) {
            return ih.a;
        }
        if (string.contains("unix")) {
            return ih.a;
        }
        return ih.e;
    }

    public void a(bh bh2) {
        if (this.p instanceof as) {
            return;
        }
        if (this.p != null) {
            this.p.h();
        }
        if (bh2 == null && this.e == null) {
            bh2 = new cx();
        } else if (bh2 == null && this.g.E <= 0) {
            bh2 = new au();
        }
        this.p = bh2;
        if (bh2 != null) {
            this.f();
            iy iy2 = new iy(this.c, this.d);
            int n2 = iy2.a();
            int n3 = iy2.b();
            bh2.a(this, n2, n3);
            this.v = false;
        } else {
            this.e();
        }
    }

    private void c(String string) {
        int n2 = GL11.glGetError();
        if (n2 != 0) {
            String string2 = GLU.gluErrorString((int)n2);
            System.out.println("########## GL ERROR ##########");
            System.out.println("@ " + string);
            System.out.println(n2 + ": " + string2);
            System.exit(0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void c() {
        if (this.z != null) {
            this.z.c();
        }
        try {
            if (this.N != null) {
                this.N.b();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            System.out.println("Stopping!");
            this.a((cn)null);
            try {
                df.a();
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.A.b();
            Mouse.destroy();
            Keyboard.destroy();
        }
        finally {
            Display.destroy();
        }
        System.gc();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        this.F = true;
        try {
            this.a();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.a(new go("Failed to start game", exception));
            return;
        }
        try {
            long l2 = System.currentTimeMillis();
            int n2 = 0;
            while (this.F && (this.z == null || this.z.isActive())) {
                cf.a();
                aj.a();
                if (this.k == null && Display.isCloseRequested()) {
                    this.d();
                }
                if (this.m && this.e != null) {
                    float f2 = this.M.c;
                    this.M.a();
                    this.M.c = f2;
                } else {
                    this.M.a();
                }
                for (int i2 = 0; i2 < this.M.b; ++i2) {
                    ++this.O;
                    try {
                        this.i();
                        continue;
                    }
                    catch (lx lx2) {
                        this.e = null;
                        this.a((cn)null);
                        this.a(new iv());
                    }
                }
                this.c("Pre render");
                this.A.a(this.g, this.M.c);
                GL11.glEnable((int)3553);
                if (this.e != null) {
                    while (this.e.e()) {
                    }
                }
                if (!this.v) {
                    if (this.b != null) {
                        this.b.a(this.M.c);
                    }
                    this.r.b(this.M.c);
                }
                if (!Display.isActive()) {
                    if (this.a) {
                        this.h();
                    }
                    Thread.sleep(10L);
                }
                if (Keyboard.isKeyDown((int)64)) {
                    this.r();
                } else {
                    this.H = System.nanoTime();
                }
                Thread.yield();
                Display.update();
                if (!(this.k == null || this.a || this.k.getWidth() == this.c && this.k.getHeight() == this.d)) {
                    this.c = this.k.getWidth();
                    this.d = this.k.getHeight();
                    if (this.c <= 0) {
                        this.c = 1;
                    }
                    if (this.d <= 0) {
                        this.d = 1;
                    }
                    this.a(this.c, this.d);
                }
                if (this.y.h) {
                    Thread.sleep(5L);
                }
                this.c("Post render");
                ++n2;
                boolean bl2 = this.m = !this.j() && this.p != null && this.p.b();
                while (System.currentTimeMillis() >= l2 + 1000L) {
                    this.G = n2 + " fps, " + bn.b + " chunk updates";
                    bn.b = 0;
                    l2 += 1000L;
                    n2 = 0;
                }
            }
        }
        catch (nr nr2) {
        }
        catch (Throwable throwable) {
            this.e = null;
            throwable.printStackTrace();
            this.a(new go("Unexpected error", throwable));
        }
    }

    private void r() {
        int n2;
        if (this.H == -1L) {
            this.H = System.nanoTime();
        }
        long l2 = System.nanoTime();
        Minecraft.D[Minecraft.E++ & Minecraft.D.length - 1] = l2 - this.H;
        this.H = l2;
        GL11.glClear((int)256);
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        GL11.glOrtho((double)0.0, (double)this.c, (double)this.d, (double)0.0, (double)1000.0, (double)3000.0);
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-2000.0f);
        GL11.glLineWidth((float)1.0f);
        GL11.glDisable((int)3553);
        ho ho2 = ho.a;
        ho2.a(7);
        ho2.b(0x20200000);
        ho2.a(0.0, (double)(this.d - 100), 0.0);
        ho2.a(0.0, (double)this.d, 0.0);
        ho2.a((double)D.length, (double)this.d, 0.0);
        ho2.a((double)D.length, (double)(this.d - 100), 0.0);
        ho2.a();
        long l3 = 0L;
        for (n2 = 0; n2 < D.length; ++n2) {
            l3 += D[n2];
        }
        n2 = (int)(l3 / 200000L / (long)D.length);
        ho2.a(7);
        ho2.b(0x20400000);
        ho2.a(0.0, (double)(this.d - n2), 0.0);
        ho2.a(0.0, (double)this.d, 0.0);
        ho2.a((double)D.length, (double)this.d, 0.0);
        ho2.a((double)D.length, (double)(this.d - n2), 0.0);
        ho2.a();
        ho2.a(1);
        for (int i2 = 0; i2 < D.length; ++i2) {
            int n3 = (i2 - E & D.length - 1) * 255 / D.length;
            int n4 = n3 * n3 / 255;
            n4 = n4 * n4 / 255;
            int n5 = n4 * n4 / 255;
            n5 = n5 * n5 / 255;
            ho2.b(-16777216 + n5 + n4 * 256 + n3 * 65536);
            long l4 = D[i2] / 200000L;
            ho2.a((double)((float)i2 + 0.5f), (double)((float)((long)this.d - l4) + 0.5f), 0.0);
            ho2.a((double)((float)i2 + 0.5f), (double)((float)this.d + 0.5f), 0.0);
        }
        ho2.a();
        GL11.glEnable((int)3553);
    }

    public void d() {
        this.F = false;
    }

    public void e() {
        if (!Display.isActive()) {
            return;
        }
        if (this.I) {
            return;
        }
        this.I = true;
        this.B.a();
        this.a((bh)null);
        this.X = this.O + 10000;
    }

    public void f() {
        if (!this.I) {
            return;
        }
        if (this.g != null) {
            this.g.k();
        }
        this.I = false;
        this.B.b();
    }

    public void g() {
        if (this.p != null) {
            return;
        }
        this.a(new ie());
    }

    private void a(int n2, boolean bl2) {
        if (this.b.b) {
            return;
        }
        if (n2 == 0 && this.P > 0) {
            return;
        }
        if (bl2 && this.x != null && this.x.a == 0 && n2 == 0) {
            int n3 = this.x.b;
            int n4 = this.x.c;
            int n5 = this.x.d;
            this.b.c(n3, n4, n5, this.x.e);
            this.h.a(n3, n4, n5, this.x.e);
        } else {
            this.b.a();
        }
    }

    private void a(int n2) {
        ev ev2;
        int n3;
        if (n2 == 0 && this.P > 0) {
            return;
        }
        if (n2 == 0) {
            this.g.w();
        }
        if (this.x == null) {
            if (n2 == 0 && !(this.b instanceof il)) {
                this.P = 10;
            }
        } else if (this.x.a == 1) {
            if (n2 == 0) {
                this.g.a(this.x.g);
            }
            if (n2 == 1) {
                this.g.a_(this.x.g);
            }
        } else if (this.x.a == 0) {
            int n4 = this.x.b;
            n3 = this.x.c;
            int n5 = this.x.d;
            int n6 = this.x.e;
            ly ly2 = ly.n[this.e.a(n4, n3, n5)];
            if (n2 == 0) {
                this.e.i(n4, n3, n5, this.x.e);
                if (ly2 != ly.A || this.g.c >= 100) {
                    this.b.a(n4, n3, n5, this.x.e);
                }
            } else {
                int n7;
                ev ev3 = this.g.b.a();
                int n8 = n7 = ev3 != null ? ev3.a : 0;
                if (this.b.a(this.g, this.e, ev3, n4, n3, n5, n6)) {
                    this.g.w();
                }
                if (ev3 == null) {
                    return;
                }
                if (ev3.a == 0) {
                    this.g.b.a[this.g.b.d] = null;
                } else if (ev3.a != n7) {
                    this.r.a.b();
                }
            }
        }
        if (n2 == 1 && (ev2 = this.g.b.a()) != null) {
            n3 = ev2.a;
            ev ev4 = ev2.a(this.e, this.g);
            if (ev4 != ev2 || ev4 != null && ev4.a != n3) {
                this.g.b.a[this.g.b.d] = ev4;
                this.r.a.c();
                if (ev4.a == 0) {
                    this.g.b.a[this.g.b.d] = null;
                }
            }
        }
    }

    public void h() {
        try {
            this.a = !this.a;
            System.out.println("Toggle fullscreen!");
            if (this.a) {
                Display.setDisplayMode((DisplayMode)Display.getDesktopDisplayMode());
                this.c = Display.getDisplayMode().getWidth();
                this.d = Display.getDisplayMode().getHeight();
                if (this.c <= 0) {
                    this.c = 1;
                }
                if (this.d <= 0) {
                    this.d = 1;
                }
            } else {
                if (this.k != null) {
                    this.c = this.k.getWidth();
                    this.d = this.k.getHeight();
                } else {
                    this.c = this.Q;
                    this.d = this.R;
                }
                if (this.c <= 0) {
                    this.c = 1;
                }
                if (this.d <= 0) {
                    this.d = 1;
                }
                Display.setDisplayMode((DisplayMode)new DisplayMode(this.Q, this.R));
            }
            this.f();
            Display.setFullscreen((boolean)this.a);
            Display.update();
            Thread.sleep(1000L);
            if (this.a) {
                this.e();
            }
            if (this.p != null) {
                this.f();
                this.a(this.c, this.d);
            }
            System.out.println("Size: " + this.c + ", " + this.d);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void a(int n2, int n3) {
        if (n2 <= 0) {
            n2 = 1;
        }
        if (n3 <= 0) {
            n3 = 1;
        }
        this.c = n2;
        this.d = n3;
        if (this.p != null) {
            iy iy2 = new iy(n2, n3);
            int n4 = iy2.a();
            int n5 = iy2.b();
            this.p.a(this, n4, n5);
        }
    }

    private void s() {
        if (this.x != null) {
            int n2 = this.e.a(this.x.b, this.x.c, this.x.d);
            if (n2 == ly.v.bc) {
                n2 = ly.w.bc;
            }
            if (n2 == ly.ak.bc) {
                n2 = ly.al.bc;
            }
            if (n2 == ly.A.bc) {
                n2 = ly.u.bc;
            }
            this.g.b.a(n2, this.b instanceof il);
        }
    }

    public void i() {
        this.u.a();
        this.r.a(1.0f);
        if (this.g != null) {
            this.g.n();
        }
        if (!this.m && this.e != null) {
            this.b.c();
        }
        GL11.glBindTexture((int)3553, (int)this.n.a("/terrain.png"));
        if (!this.m) {
            this.n.a();
        }
        if (this.p == null && this.g != null && this.g.E <= 0) {
            this.a((bh)null);
        }
        if (this.p != null) {
            this.X = this.O + 10000;
        }
        if (this.p != null) {
            this.p.d();
            if (this.p != null) {
                this.p.g();
            }
        }
        if (this.p == null || this.p.f) {
            while (Mouse.next()) {
                long l2 = System.currentTimeMillis() - this.K;
                if (l2 > 200L) continue;
                int n2 = Mouse.getEventDWheel();
                if (n2 != 0) {
                    this.g.b.a(n2);
                }
                if (this.p == null) {
                    if (!this.I && Mouse.getEventButtonState()) {
                        this.e();
                        continue;
                    }
                    if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                        this.a(0);
                        this.X = this.O;
                    }
                    if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                        this.a(1);
                        this.X = this.O;
                    }
                    if (Mouse.getEventButton() != 2 || !Mouse.getEventButtonState()) continue;
                    this.s();
                    continue;
                }
                if (this.p == null) continue;
                this.p.e();
            }
            if (this.P > 0) {
                --this.P;
            }
            while (Keyboard.next()) {
                this.g.a(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if (!Keyboard.getEventKeyState()) continue;
                if (Keyboard.getEventKey() == 87) {
                    this.h();
                    continue;
                }
                if (this.p != null) {
                    this.p.f();
                } else {
                    if (Keyboard.getEventKey() == 1) {
                        this.g();
                    }
                    if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown((int)61)) {
                        this.t();
                    }
                    if (Keyboard.getEventKey() == 63) {
                        boolean bl2 = this.y.x = !this.y.x;
                    }
                    if (Keyboard.getEventKey() == this.y.o.b) {
                        this.a(new lo(this.g.b, this.g.b.c));
                    }
                    if (Keyboard.getEventKey() == this.y.p.b) {
                        this.g.a(this.g.b.a(this.g.b.d, 1), false);
                    }
                    if (this.j() && Keyboard.getEventKey() == this.y.q.b) {
                        this.a(new de());
                    }
                }
                for (int i2 = 0; i2 < 9; ++i2) {
                    if (Keyboard.getEventKey() != 2 + i2) continue;
                    this.g.b.d = i2;
                }
                if (Keyboard.getEventKey() != this.y.r.b) continue;
                this.y.b(4, Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54) ? -1 : 1);
            }
            if (this.p == null) {
                if (Mouse.isButtonDown((int)0) && (float)(this.O - this.X) >= this.M.a / 4.0f && this.I) {
                    this.a(0);
                    this.X = this.O;
                }
                if (Mouse.isButtonDown((int)1) && (float)(this.O - this.X) >= this.M.a / 4.0f && this.I) {
                    this.a(1);
                    this.X = this.O;
                }
            }
            this.a(0, this.p == null && Mouse.isButtonDown((int)0) && this.I);
        }
        if (this.e != null) {
            if (this.g != null) {
                ++this.Y;
                if (this.Y == 30) {
                    this.Y = 0;
                    this.e.f(this.g);
                }
            }
            this.e.l = this.y.w;
            if (!this.m) {
                this.r.a();
            }
            if (!this.m) {
                this.f.d();
            }
            if (!this.m) {
                this.e.c();
            }
            if (!this.m || this.j()) {
                this.e.g();
            }
            if (!this.m && this.e != null) {
                this.e.m(eo.b(this.g.ak), eo.b(this.g.al), eo.b(this.g.am));
            }
            if (!this.m) {
                this.h.a();
            }
        }
        this.K = System.currentTimeMillis();
    }

    private void t() {
        System.out.println("FORCING RELOAD!");
        this.A = new of();
        this.A.a(this.y);
        this.N.a();
    }

    public boolean j() {
        return this.e != null && this.e.y;
    }

    public void b(String string) {
        this.a((cn)null);
        System.gc();
        cn cn2 = new cn(new File(Minecraft.b(), "saves"), string);
        if (cn2.r) {
            this.a(cn2, "Generating level");
        } else {
            this.a(cn2, "Loading level");
        }
    }

    public void a(cn cn2) {
        this.a(cn2, "");
    }

    public void a(cn cn2, String string) {
        this.A.a(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        if (this.e != null) {
            this.e.a(this.q);
        }
        this.e = cn2;
        if (cn2 != null) {
            this.b.a(cn2);
            cn2.m = this.o;
            if (!this.j()) {
                this.g = (bi)cn2.a(bi.class);
            } else if (this.g != null) {
                this.g.q();
                if (cn2 != null) {
                    cn2.a((kh)this.g);
                }
            }
            if (!cn2.y) {
                this.d(string);
            }
            if (this.g == null) {
                this.g = (bi)this.b.b(cn2);
                this.g.q();
                this.b.a(this.g);
            }
            this.g.a = new gd(this.y);
            if (this.f != null) {
                this.f.a(cn2);
            }
            if (this.h != null) {
                this.h.a(cn2);
            }
            this.b.b(this.g);
            cn2.a(this.g);
            if (cn2.r) {
                cn2.a(this.q);
            }
        } else {
            this.g = null;
        }
        System.gc();
        this.K = 0L;
    }

    private void d(String string) {
        this.q.a(string);
        this.q.d("Building terrain");
        int n2 = 128;
        int n3 = 0;
        int n4 = n2 * 2 / 16 + 1;
        n4 *= n4;
        for (int i2 = -n2; i2 <= n2; i2 += 16) {
            int n5 = this.e.o;
            int n6 = this.e.q;
            if (this.g != null) {
                n5 = (int)this.g.ak;
                n6 = (int)this.g.am;
            }
            for (int i3 = -n2; i3 <= n2; i3 += 16) {
                this.q.a(n3++ * 100 / n4);
                this.e.a(n5 + i2, 64, n6 + i3);
                while (this.e.e()) {
                }
            }
        }
        this.q.d("Simulating world for a bit");
        n4 = 2000;
        this.e.j();
    }

    public void a(String string, File file) {
        int n2 = string.indexOf("/");
        String string2 = string.substring(0, n2);
        string = string.substring(n2 + 1);
        if (string2.equalsIgnoreCase("sound")) {
            this.A.a(string, file);
        } else if (string2.equalsIgnoreCase("newsound")) {
            this.A.a(string, file);
        } else if (string2.equalsIgnoreCase("streaming")) {
            this.A.b(string, file);
        } else if (string2.equalsIgnoreCase("music")) {
            this.A.c(string, file);
        } else if (string2.equalsIgnoreCase("newmusic")) {
            this.A.c(string, file);
        }
    }

    public be k() {
        return this.L;
    }

    public String l() {
        return this.f.b();
    }

    public String m() {
        return this.f.c();
    }

    public String n() {
        return "P: " + this.h.b() + ". T: " + this.e.d();
    }

    public void o() {
        this.e.a();
        if (this.g != null) {
            this.e.d(this.g);
        }
        this.g = (bi)this.b.b(this.e);
        this.g.q();
        this.b.a(this.g);
        this.e.a(this.g);
        this.g.a = new gd(this.y);
        this.b.b(this.g);
        this.d("Respawning");
    }

    public static void a(String string, String string2) {
        Minecraft.a(string, string2, null);
    }

    public static void a(String string, String string2, String string3) {
        boolean bl2 = false;
        String string4 = string;
        Frame frame = new Frame("Minecraft");
        Canvas canvas = new Canvas();
        frame.setLayout(new BorderLayout());
        frame.add((Component)canvas, "Center");
        canvas.setPreferredSize(new Dimension(854, 480));
        frame.pack();
        frame.setLocationRelativeTo(null);
        fm fm2 = new fm(frame, canvas, null, 854, 480, bl2, frame);
        Thread thread = new Thread((Runnable)fm2, "Minecraft main thread");
        thread.setPriority(10);
        fm2.l = false;
        fm2.j = "www.minecraft.net";
        fm2.i = string4 != null && string2 != null ? new dl(string4, string2) : new dl("Player" + System.currentTimeMillis() % 1000L, "");
        if (string3 != null) {
            String[] stringArray = string3.split(":");
            fm2.a(stringArray[0], Integer.parseInt(stringArray[1]));
        }
        frame.setVisible(true);
        frame.addWindowListener(new fp(fm2, thread));
        thread.start();
    }

    public static void main(String[] stringArray) {
        String string = "Player" + System.currentTimeMillis() % 1000L;
        if (stringArray.length > 0) {
            string = stringArray[0];
        }
        String string2 = "-";
        if (stringArray.length > 1) {
            string2 = stringArray[1];
        }
        Minecraft.a(string, string2);
    }
}

