/*
 * Decompiled with CFR 0.152.
 */
public class mr
extends di {
    private static final int[] ba = new int[]{3, 8, 6, 3};
    private static final int[] bb = new int[]{11, 16, 15, 13};
    public final int a;
    public final int aX;
    public final int aY;
    public final int aZ;

    public mr(int n2, int n3, int n4, int n5) {
        super(n2);
        this.a = n3;
        this.aX = n5;
        this.aZ = n4;
        this.aY = ba[n5];
        this.aU = bb[n5] * 3 << n3;
        this.aT = 1;
    }
}

