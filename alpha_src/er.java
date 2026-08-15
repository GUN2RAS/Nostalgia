/*
 * Decompiled with CFR 0.152.
 */
public enum er {
    a("Kebab", 16, 16, 0, 0),
    b("Aztec", 16, 16, 16, 0),
    c("Alban", 16, 16, 32, 0),
    d("Aztec2", 16, 16, 48, 0),
    e("Bomb", 16, 16, 64, 0),
    f("Plant", 16, 16, 80, 0),
    g("Wasteland", 16, 16, 96, 0),
    h("Pool", 32, 16, 0, 32),
    i("Courbet", 32, 16, 32, 32),
    j("Sea", 32, 16, 64, 32),
    k("Sunset", 32, 16, 96, 32),
    l("Creebet", 32, 16, 128, 32),
    m("Wanderer", 16, 32, 0, 64),
    n("Graham", 16, 32, 16, 64),
    o("Match", 32, 32, 0, 128),
    p("Bust", 32, 32, 32, 128),
    q("Stage", 32, 32, 64, 128),
    r("Void", 32, 32, 96, 128),
    s("SkullAndRoses", 32, 32, 128, 128),
    t("Fighters", 64, 32, 0, 96),
    u("Pointer", 64, 64, 0, 192),
    v("Pigscene", 64, 64, 64, 192),
    w("Skeleton", 64, 48, 192, 64),
    x("DonkeyKong", 64, 48, 192, 112);

    public final String y;
    public final int z;
    public final int A;
    public final int B;
    public final int C;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private er(int n2, int n3, int n4) {
        void var7_5;
        void var6_4;
        this.y = (String)n2;
        this.z = n3;
        this.A = n4;
        this.B = var6_4;
        this.C = var7_5;
    }
}

