/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class dw {
    private static final dw a = new dw();
    private List b = new ArrayList();

    public static final dw a() {
        return a;
    }

    private dw() {
        new dr().a(this);
        new nd().a(this);
        new lf().a(this);
        new hr().a(this);
        new fj().a(this);
        new l().a(this);
        this.a(new ev(di.aI, 3), "###", Character.valueOf('#'), di.aH);
        this.a(new ev(di.aJ, 1), "#", "#", "#", Character.valueOf('#'), di.aI);
        this.a(new ev(ly.ba, 2), "###", "###", Character.valueOf('#'), di.B);
        this.a(new ev(ly.aZ, 1), "###", "#X#", "###", Character.valueOf('#'), ly.y, Character.valueOf('X'), di.l);
        this.a(new ev(ly.ao, 1), "###", "XXX", "###", Character.valueOf('#'), ly.y, Character.valueOf('X'), di.aJ);
        this.a(new ev(ly.aV, 1), "##", "##", Character.valueOf('#'), di.aB);
        this.a(new ev(ly.aX, 1), "##", "##", Character.valueOf('#'), di.aG);
        this.a(new ev(ly.am, 1), "##", "##", Character.valueOf('#'), di.aF);
        this.a(new ev(ly.ac, 1), "###", "###", "###", Character.valueOf('#'), di.I);
        this.a(new ev(ly.an, 1), "X#X", "#X#", "X#X", Character.valueOf('X'), di.K, Character.valueOf('#'), ly.F);
        this.a(new ev(ly.al, 3), "###", Character.valueOf('#'), ly.x);
        this.a(new ev(ly.aG, 1), "# #", "###", "# #", Character.valueOf('#'), di.B);
        this.a(new ev(di.at, 1), "##", "##", "##", Character.valueOf('#'), ly.y);
        this.a(new ev(di.az, 1), "##", "##", "##", Character.valueOf('#'), di.m);
        this.a(new ev(di.as, 1), "###", "###", " X ", Character.valueOf('#'), ly.y, Character.valueOf('X'), di.B);
        this.a(new ev(ly.y, 4), "#", Character.valueOf('#'), ly.K);
        this.a(new ev(di.B, 4), "#", "#", Character.valueOf('#'), ly.y);
        this.a(new ev(ly.ar, 4), "X", "#", Character.valueOf('X'), di.k, Character.valueOf('#'), di.B);
        this.a(new ev(di.C, 4), "# #", " # ", Character.valueOf('#'), ly.y);
        this.a(new ev(ly.aH, 16), "X X", "X#X", "X X", Character.valueOf('X'), di.m, Character.valueOf('#'), di.B);
        this.a(new ev(di.ax, 1), "# #", "###", Character.valueOf('#'), di.m);
        this.a(new ev(di.aL, 1), "A", "B", Character.valueOf('A'), ly.av, Character.valueOf('B'), di.ax);
        this.a(new ev(di.aM, 1), "A", "B", Character.valueOf('A'), ly.aC, Character.valueOf('B'), di.ax);
        this.a(new ev(di.aC, 1), "# #", "###", Character.valueOf('#'), ly.y);
        this.a(new ev(di.au, 1), "# #", " # ", Character.valueOf('#'), di.m);
        this.a(new ev(di.g, 1), "A ", " B", Character.valueOf('A'), di.m, Character.valueOf('B'), di.an);
        this.a(new ev(di.S, 1), "###", Character.valueOf('#'), di.R);
        this.a(new ev(ly.au, 4), "#  ", "## ", "###", Character.valueOf('#'), ly.y);
        this.a(new ev(di.aP, 1), "  #", " #X", "# X", Character.valueOf('#'), di.B, Character.valueOf('X'), di.I);
        this.a(new ev(ly.aI, 4), "#  ", "## ", "###", Character.valueOf('#'), ly.x);
        this.a(new ev(di.aq, 1), "###", "#X#", "###", Character.valueOf('#'), di.B, Character.valueOf('X'), ly.ac);
        this.a(new ev(di.ar, 1), "###", "#X#", "###", Character.valueOf('#'), ly.ai, Character.valueOf('X'), di.h);
        this.a(new ev(ly.aK, 1), "X", "#", Character.valueOf('#'), ly.x, Character.valueOf('X'), di.B);
        this.a(new ev(ly.aR, 1), "X", "#", Character.valueOf('#'), di.B, Character.valueOf('X'), di.aA);
        this.a(new ev(di.aO, 1), " # ", "#X#", " # ", Character.valueOf('#'), di.m, Character.valueOf('X'), di.aA);
        this.a(new ev(ly.aS, 1), "#", "#", Character.valueOf('#'), ly.u);
        this.a(new ev(ly.aL, 1), "###", Character.valueOf('#'), ly.u);
        this.a(new ev(ly.aN, 1), "###", Character.valueOf('#'), ly.y);
        Collections.sort(this.b, new fs(this));
        System.out.println(this.b.size() + " recipes");
    }

    void a(ev ev2, Object ... objectArray) {
        Object object;
        String string = "";
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        if (objectArray[n2] instanceof String[]) {
            object = (String[])objectArray[n2++];
            for (int i2 = 0; i2 < ((String[])object).length; ++i2) {
                Object object2 = object[i2];
                ++n4;
                n3 = ((String)object2).length();
                string = string + (String)object2;
            }
        } else {
            while (objectArray[n2] instanceof String) {
                object = (String)objectArray[n2++];
                ++n4;
                n3 = ((String)object).length();
                string = string + (String)object;
            }
        }
        object = new HashMap();
        while (n2 < objectArray.length) {
            Character c2 = (Character)objectArray[n2];
            int n5 = 0;
            if (objectArray[n2 + 1] instanceof di) {
                n5 = ((di)objectArray[n2 + 1]).aS;
            } else if (objectArray[n2 + 1] instanceof ly) {
                n5 = ((ly)objectArray[n2 + 1]).bc;
            }
            object.put(c2, n5);
            n2 += 2;
        }
        int[] nArray = new int[n3 * n4];
        for (int i3 = 0; i3 < n3 * n4; ++i3) {
            char c3 = string.charAt(i3);
            nArray[i3] = object.containsKey(Character.valueOf(c3)) ? (Integer)object.get(Character.valueOf(c3)) : -1;
        }
        this.b.add(new bv(n3, n4, nArray, ev2));
    }

    public ev a(int[] nArray) {
        for (int i2 = 0; i2 < this.b.size(); ++i2) {
            bv bv2 = (bv)this.b.get(i2);
            if (!bv2.a(nArray)) continue;
            return bv2.b(nArray);
        }
        return null;
    }
}

