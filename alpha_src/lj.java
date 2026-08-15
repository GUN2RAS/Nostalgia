/*
 * Decompiled with CFR 0.152.
 */
class lj
extends mm {
    final /* synthetic */ int c;
    final /* synthetic */ lo d;

    lj(lo lo2, ee ee2, gh gh2, int n2, int n3, int n4, int n5) {
        this.d = lo2;
        this.c = n5;
        super(ee2, gh2, n2, n3, n4);
    }

    public boolean a(ev ev2) {
        if (ev2.a() instanceof mr) {
            return ((mr)ev2.a()).aX == this.c;
        }
        return false;
    }

    public int c() {
        return 15 + this.c * 16;
    }
}

