/*
 * Decompiled with CFR 0.152.
 */
public class y
extends bs {
    private static ly[] aX = new ly[]{ly.x, ly.ak, ly.al, ly.u, ly.ap, ly.I, ly.aj, ly.J, ly.ai, ly.H, ly.ax, ly.ay, ly.aU};
    private int aY;

    public y(int n2, int n3) {
        super(n2, 2, n3, aX);
        this.aY = n3;
    }

    public boolean a(ly ly2) {
        if (ly2 == ly.aq) {
            return this.aY == 3;
        }
        if (ly2 == ly.ay || ly2 == ly.ax) {
            return this.aY >= 2;
        }
        if (ly2 == ly.ai || ly2 == ly.H) {
            return this.aY >= 2;
        }
        if (ly2 == ly.aj || ly2 == ly.I) {
            return this.aY >= 1;
        }
        if (ly2 == ly.aO || ly2 == ly.aP) {
            return this.aY >= 2;
        }
        if (ly2.bn == gb.d) {
            return true;
        }
        return ly2.bn == gb.e;
    }
}

