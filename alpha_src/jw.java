/*
 * Decompiled with CFR 0.152.
 */
public class jw
extends di {
    public jw(int n2) {
        super(n2);
        this.aT = 1;
        this.aU = 64;
    }

    public void b(ev ev2, ge ge2) {
        if (ge2 instanceof mv) {
            mv mv2 = (mv)ge2;
            if (!mv2.a) {
                mv2.a = true;
                --ev2.a;
            }
        }
    }

    public void a(ev ev2, ge ge2) {
        this.b(ev2, ge2);
    }
}

