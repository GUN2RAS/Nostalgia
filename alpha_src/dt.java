/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class dt
extends mq {
    protected dt(int n2, int n3) {
        super(n2, n3);
        float f2 = 0.4f;
        this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, f2 * 2.0f, 0.5f + f2);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        super.a(cn2, n2, n3, n4, random);
        if (cn2.j(n2, n3 + 1, n4) >= 9 && random.nextInt(5) == 0) {
            int n5 = cn2.e(n2, n3, n4);
            if (n5 < 15) {
                cn2.b(n2, n3, n4, n5 + 1);
            } else {
                cn2.a(n2, n3, n4, 0);
                ik ik2 = new oa();
                if (random.nextInt(10) == 0) {
                    ik2 = new ej();
                }
                if (!((ik)ik2).a(cn2, random, n2, n3, n4)) {
                    cn2.a(n2, n3, n4, this.bc);
                }
            }
        }
    }
}

