/*
 * Decompiled with CFR 0.152.
 */
public class jg
extends di {
    public jg(int n2) {
        super(n2);
        this.aT = 1;
    }

    public ev a(ev ev2, cn cn2, dm dm2) {
        if (dm2.b.b(di.j.aS)) {
            cn2.a(dm2, "random.bow", 1.0f, 1.0f / (b.nextFloat() * 0.4f + 0.8f));
            cn2.a(new kg(cn2, dm2));
        }
        return ev2;
    }
}

