/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ii {
    public static final Object a = new Object();
    public static int b;
    public static int c;
    private Object d = new Object();
    private Socket e;
    private DataInputStream f;
    private DataOutputStream g;
    private boolean h = true;
    private List i = Collections.synchronizedList(new LinkedList());
    private List j = Collections.synchronizedList(new LinkedList());
    private List k = Collections.synchronizedList(new LinkedList());
    private lb l;
    private boolean m = false;
    private Thread n;
    private Thread o;
    private boolean p = false;
    private String q = "";
    private int r = 0;
    private int s = 0;
    private int t = 0;

    public ii(Socket socket, String string, lb lb2) {
        this.e = socket;
        this.l = lb2;
        socket.setTrafficClass(24);
        this.f = new DataInputStream(socket.getInputStream());
        this.g = new DataOutputStream(socket.getOutputStream());
        this.o = new ni(this, string + " read thread");
        this.n = new nk(this, string + " write thread");
        this.o.start();
        this.n.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void a(fn fn2) {
        if (this.m) {
            return;
        }
        Object object = this.d;
        synchronized (object) {
            this.s += fn2.a() + 1;
            if (fn2.j) {
                this.k.add(fn2);
            } else {
                this.j.add(fn2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void b() {
        block12: {
            try {
                fn fn2;
                Object object;
                boolean bl2 = true;
                if (!this.j.isEmpty()) {
                    bl2 = false;
                    object = this.d;
                    synchronized (object) {
                        fn2 = (fn)this.j.remove(0);
                        this.s -= fn2.a() + 1;
                    }
                    fn.a(fn2, this.g);
                }
                if ((bl2 || this.t-- <= 0) && !this.k.isEmpty()) {
                    bl2 = false;
                    object = this.d;
                    synchronized (object) {
                        fn2 = (fn)this.k.remove(0);
                        this.s -= fn2.a() + 1;
                    }
                    fn.a(fn2, this.g);
                    this.t = 50;
                }
                if (bl2) {
                    Thread.sleep(10L);
                }
            }
            catch (InterruptedException interruptedException) {
            }
            catch (Exception exception) {
                if (this.p) break block12;
                this.a(exception);
            }
        }
    }

    private void c() {
        block4: {
            try {
                fn fn2 = fn.b(this.f);
                if (fn2 != null) {
                    this.i.add(fn2);
                } else {
                    this.a("End of stream");
                }
            }
            catch (Exception exception) {
                if (this.p) break block4;
                this.a(exception);
            }
        }
    }

    private void a(Exception exception) {
        exception.printStackTrace();
        this.a("Internal exception: " + exception.toString());
    }

    public void a(String string) {
        if (!this.h) {
            return;
        }
        this.p = true;
        this.q = string;
        new nh(this).start();
        this.h = false;
        try {
            this.f.close();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            this.g.close();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            this.e.close();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void a() {
        if (this.s > 0x100000) {
            this.a("Send buffer overflow");
        }
        if (this.i.isEmpty()) {
            if (this.r++ == 1200) {
                this.a("Timed out");
            }
        } else {
            this.r = 0;
        }
        int n2 = 100;
        while (!this.i.isEmpty() && n2-- >= 0) {
            fn fn2 = (fn)this.i.remove(0);
            fn2.a(this.l);
        }
        if (this.p && this.i.isEmpty()) {
            this.l.a(this.q);
        }
    }

    static /* synthetic */ boolean a(ii ii2) {
        return ii2.h;
    }

    static /* synthetic */ boolean b(ii ii2) {
        return ii2.m;
    }

    static /* synthetic */ void c(ii ii2) {
        ii2.c();
    }

    static /* synthetic */ void d(ii ii2) {
        ii2.b();
    }

    static /* synthetic */ Thread e(ii ii2) {
        return ii2.o;
    }

    static /* synthetic */ Thread f(ii ii2) {
        return ii2.n;
    }
}

