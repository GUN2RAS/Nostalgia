/*
 * Decompiled with CFR 0.152.
 */
public class bp
extends di {
    public bp(int n2) {
        super(n2);
        this.aT = 16;
    }

    public ev a(ev ev2, cn cn2, dm dm2) {
        --ev2.a;
        cn2.a(dm2, "random.bow", 0.5f, 0.4f / (b.nextFloat() * 0.4f + 0.8f));
        cn2.a(new ao(cn2, dm2));
        return ev2;
    }
}

