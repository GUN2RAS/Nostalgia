/*
 * Decompiled with CFR 0.152.
 */
class k
extends az {
    final /* synthetic */ ia a;

    k(ia ia2, int n2, Class clazz, Class[] classArray) {
        this.a = ia2;
        super(n2, clazz, classArray);
    }

    protected mt a(cn cn2, int n2, int n3) {
        int n4 = n2 + cn2.n.nextInt(16);
        int n5 = cn2.n.nextInt(cn2.n.nextInt(120) + 8);
        int n6 = n3 + cn2.n.nextInt(16);
        return new mt(n4, n5, n6);
    }
}

