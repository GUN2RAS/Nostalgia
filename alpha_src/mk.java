/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.List;

class mk {
    private cn b;
    private int c;
    private int d;
    private int e;
    private int f;
    private List g = new ArrayList();
    final /* synthetic */ if a;

    public mk(if if_, cn cn2, int n2, int n3, int n4) {
        this.a = if_;
        this.b = cn2;
        this.c = n2;
        this.d = n3;
        this.e = n4;
        this.f = cn2.e(n2, n3, n4);
        this.a();
    }

    private void a() {
        this.g.clear();
        if (this.f == 0) {
            this.g.add(new mt(this.c, this.d, this.e - 1));
            this.g.add(new mt(this.c, this.d, this.e + 1));
        } else if (this.f == 1) {
            this.g.add(new mt(this.c - 1, this.d, this.e));
            this.g.add(new mt(this.c + 1, this.d, this.e));
        } else if (this.f == 2) {
            this.g.add(new mt(this.c - 1, this.d, this.e));
            this.g.add(new mt(this.c + 1, this.d + 1, this.e));
        } else if (this.f == 3) {
            this.g.add(new mt(this.c - 1, this.d + 1, this.e));
            this.g.add(new mt(this.c + 1, this.d, this.e));
        } else if (this.f == 4) {
            this.g.add(new mt(this.c, this.d + 1, this.e - 1));
            this.g.add(new mt(this.c, this.d, this.e + 1));
        } else if (this.f == 5) {
            this.g.add(new mt(this.c, this.d, this.e - 1));
            this.g.add(new mt(this.c, this.d + 1, this.e + 1));
        } else if (this.f == 6) {
            this.g.add(new mt(this.c + 1, this.d, this.e));
            this.g.add(new mt(this.c, this.d, this.e + 1));
        } else if (this.f == 7) {
            this.g.add(new mt(this.c - 1, this.d, this.e));
            this.g.add(new mt(this.c, this.d, this.e + 1));
        } else if (this.f == 8) {
            this.g.add(new mt(this.c - 1, this.d, this.e));
            this.g.add(new mt(this.c, this.d, this.e - 1));
        } else if (this.f == 9) {
            this.g.add(new mt(this.c + 1, this.d, this.e));
            this.g.add(new mt(this.c, this.d, this.e - 1));
        }
    }

    private void b() {
        for (int i2 = 0; i2 < this.g.size(); ++i2) {
            mk mk2 = this.a((mt)this.g.get(i2));
            if (mk2 == null || !mk2.b(this)) {
                this.g.remove(i2--);
                continue;
            }
            this.g.set(i2, new mt(mk2.c, mk2.d, mk2.e));
        }
    }

    private boolean a(int n2, int n3, int n4) {
        if (this.b.a(n2, n3, n4) == this.a.bc) {
            return true;
        }
        if (this.b.a(n2, n3 + 1, n4) == this.a.bc) {
            return true;
        }
        return this.b.a(n2, n3 - 1, n4) == this.a.bc;
    }

    private mk a(mt mt2) {
        if (this.b.a(mt2.a, mt2.b, mt2.c) == this.a.bc) {
            return new mk(this.a, this.b, mt2.a, mt2.b, mt2.c);
        }
        if (this.b.a(mt2.a, mt2.b + 1, mt2.c) == this.a.bc) {
            return new mk(this.a, this.b, mt2.a, mt2.b + 1, mt2.c);
        }
        if (this.b.a(mt2.a, mt2.b - 1, mt2.c) == this.a.bc) {
            return new mk(this.a, this.b, mt2.a, mt2.b - 1, mt2.c);
        }
        return null;
    }

    private boolean b(mk mk2) {
        for (int i2 = 0; i2 < this.g.size(); ++i2) {
            mt mt2 = (mt)this.g.get(i2);
            if (mt2.a != mk2.c || mt2.c != mk2.e) continue;
            return true;
        }
        return false;
    }

    private boolean b(int n2, int n3, int n4) {
        for (int i2 = 0; i2 < this.g.size(); ++i2) {
            mt mt2 = (mt)this.g.get(i2);
            if (mt2.a != n2 || mt2.c != n4) continue;
            return true;
        }
        return false;
    }

    private int c() {
        int n2 = 0;
        if (this.a(this.c, this.d, this.e - 1)) {
            ++n2;
        }
        if (this.a(this.c, this.d, this.e + 1)) {
            ++n2;
        }
        if (this.a(this.c - 1, this.d, this.e)) {
            ++n2;
        }
        if (this.a(this.c + 1, this.d, this.e)) {
            ++n2;
        }
        return n2;
    }

    private boolean c(mk mk2) {
        if (this.b(mk2)) {
            return true;
        }
        if (this.g.size() == 2) {
            return false;
        }
        if (this.g.size() == 0) {
            return true;
        }
        mt mt2 = (mt)this.g.get(0);
        if (mk2.d == this.d && mt2.b == this.d) {
            return true;
        }
        return true;
    }

    private void d(mk mk2) {
        this.g.add(new mt(mk2.c, mk2.d, mk2.e));
        boolean bl2 = this.b(this.c, this.d, this.e - 1);
        boolean bl3 = this.b(this.c, this.d, this.e + 1);
        boolean bl4 = this.b(this.c - 1, this.d, this.e);
        boolean bl5 = this.b(this.c + 1, this.d, this.e);
        int n2 = -1;
        if (bl2 || bl3) {
            n2 = 0;
        }
        if (bl4 || bl5) {
            n2 = 1;
        }
        if (bl3 && bl5 && !bl2 && !bl4) {
            n2 = 6;
        }
        if (bl3 && bl4 && !bl2 && !bl5) {
            n2 = 7;
        }
        if (bl2 && bl4 && !bl3 && !bl5) {
            n2 = 8;
        }
        if (bl2 && bl5 && !bl3 && !bl4) {
            n2 = 9;
        }
        if (n2 == 0) {
            if (this.b.a(this.c, this.d + 1, this.e - 1) == this.a.bc) {
                n2 = 4;
            }
            if (this.b.a(this.c, this.d + 1, this.e + 1) == this.a.bc) {
                n2 = 5;
            }
        }
        if (n2 == 1) {
            if (this.b.a(this.c + 1, this.d + 1, this.e) == this.a.bc) {
                n2 = 2;
            }
            if (this.b.a(this.c - 1, this.d + 1, this.e) == this.a.bc) {
                n2 = 3;
            }
        }
        if (n2 < 0) {
            n2 = 0;
        }
        this.b.b(this.c, this.d, this.e, n2);
    }

    private boolean c(int n2, int n3, int n4) {
        mk mk2 = this.a(new mt(n2, n3, n4));
        if (mk2 == null) {
            return false;
        }
        mk2.b();
        return mk2.c(this);
    }

    public void a(boolean bl2) {
        boolean bl3 = this.c(this.c, this.d, this.e - 1);
        boolean bl4 = this.c(this.c, this.d, this.e + 1);
        boolean bl5 = this.c(this.c - 1, this.d, this.e);
        boolean bl6 = this.c(this.c + 1, this.d, this.e);
        int n2 = -1;
        if ((bl3 || bl4) && !bl5 && !bl6) {
            n2 = 0;
        }
        if ((bl5 || bl6) && !bl3 && !bl4) {
            n2 = 1;
        }
        if (bl4 && bl6 && !bl3 && !bl5) {
            n2 = 6;
        }
        if (bl4 && bl5 && !bl3 && !bl6) {
            n2 = 7;
        }
        if (bl3 && bl5 && !bl4 && !bl6) {
            n2 = 8;
        }
        if (bl3 && bl6 && !bl4 && !bl5) {
            n2 = 9;
        }
        if (n2 == -1) {
            if (bl3 || bl4) {
                n2 = 0;
            }
            if (bl5 || bl6) {
                n2 = 1;
            }
            if (bl2) {
                if (bl4 && bl6) {
                    n2 = 6;
                }
                if (bl5 && bl4) {
                    n2 = 7;
                }
                if (bl6 && bl3) {
                    n2 = 9;
                }
                if (bl3 && bl5) {
                    n2 = 8;
                }
            } else {
                if (bl3 && bl5) {
                    n2 = 8;
                }
                if (bl6 && bl3) {
                    n2 = 9;
                }
                if (bl5 && bl4) {
                    n2 = 7;
                }
                if (bl4 && bl6) {
                    n2 = 6;
                }
            }
        }
        if (n2 == 0) {
            if (this.b.a(this.c, this.d + 1, this.e - 1) == this.a.bc) {
                n2 = 4;
            }
            if (this.b.a(this.c, this.d + 1, this.e + 1) == this.a.bc) {
                n2 = 5;
            }
        }
        if (n2 == 1) {
            if (this.b.a(this.c + 1, this.d + 1, this.e) == this.a.bc) {
                n2 = 2;
            }
            if (this.b.a(this.c - 1, this.d + 1, this.e) == this.a.bc) {
                n2 = 3;
            }
        }
        if (n2 < 0) {
            n2 = 0;
        }
        this.f = n2;
        this.a();
        this.b.b(this.c, this.d, this.e, n2);
        for (int i2 = 0; i2 < this.g.size(); ++i2) {
            mk mk2 = this.a((mt)this.g.get(i2));
            if (mk2 == null) continue;
            mk2.b();
            if (!mk2.c(this)) continue;
            mk2.d(this);
        }
    }

    static /* synthetic */ int a(mk mk2) {
        return mk2.c();
    }
}

