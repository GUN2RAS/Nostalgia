/*
 * Decompiled with CFR 0.152.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class le
implements af {
    private File a;
    private boolean b;

    public le(File file, boolean bl2) {
        this.a = file;
        this.b = bl2;
    }

    private File a(int n2, int n3) {
        String string = "c." + Integer.toString(n2, 36) + "." + Integer.toString(n3, 36) + ".dat";
        String string2 = Integer.toString(n2 & 0x3F, 36);
        String string3 = Integer.toString(n3 & 0x3F, 36);
        File file = new File(this.a, string2);
        if (!file.exists()) {
            if (this.b) {
                file.mkdir();
            } else {
                return null;
            }
        }
        if (!(file = new File(file, string3)).exists()) {
            if (this.b) {
                file.mkdir();
            } else {
                return null;
            }
        }
        if (!(file = new File(file, string)).exists() && !this.b) {
            return null;
        }
        return file;
    }

    public ga a(cn cn2, int n2, int n3) {
        File file = this.a(n2, n3);
        if (file != null && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                hm hm2 = x.a(fileInputStream);
                if (!hm2.b("Level")) {
                    System.out.println("Chunk file at " + n2 + "," + n3 + " is missing level data, skipping");
                    return null;
                }
                if (!hm2.k("Level").b("Blocks")) {
                    System.out.println("Chunk file at " + n2 + "," + n3 + " is missing block data, skipping");
                    return null;
                }
                ga ga2 = le.a(cn2, hm2.k("Level"));
                if (!ga2.a(n2, n3)) {
                    System.out.println("Chunk file at " + n2 + "," + n3 + " is in the wrong location; relocating. (Expected " + n2 + ", " + n3 + ", got " + ga2.j + ", " + ga2.k + ")");
                    hm2.a("xPos", n2);
                    hm2.a("zPos", n3);
                    ga2 = le.a(cn2, hm2.k("Level"));
                }
                return ga2;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public void a(cn cn2, ga ga2) {
        cn2.l();
        File file = this.a(ga2.j, ga2.k);
        if (file.exists()) {
            cn2.v -= file.length();
        }
        try {
            File file2 = new File(this.a, "tmp_chunk.dat");
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            hm hm2 = new hm();
            hm hm3 = new hm();
            hm2.a("Level", (el)hm3);
            this.a(ga2, cn2, hm3);
            x.a(hm2, fileOutputStream);
            fileOutputStream.close();
            if (file.exists()) {
                file.delete();
            }
            file2.renameTo(file);
            cn2.v += file.length();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(ga ga2, cn cn2, hm hm2) {
        hm hm3;
        cn2.l();
        hm2.a("xPos", ga2.j);
        hm2.a("zPos", ga2.k);
        hm2.a("LastUpdate", cn2.c);
        hm2.a("Blocks", ga2.b);
        hm2.a("Data", ga2.e.a);
        hm2.a("SkyLight", ga2.f.a);
        hm2.a("BlockLight", ga2.g.a);
        hm2.a("HeightMap", ga2.h);
        hm2.a("TerrainPopulated", ga2.n);
        ga2.r = false;
        ki ki2 = new ki();
        for (int i2 = 0; i2 < ga2.m.length; ++i2) {
            for (Object object : ga2.m[i2]) {
                ga2.r = true;
                hm3 = new hm();
                if (!((kh)object).c(hm3)) continue;
                ki2.a(hm3);
            }
        }
        hm2.a("Entities", ki2);
        ki ki3 = new ki();
        for (Object object : ga2.l.values()) {
            hm3 = new hm();
            ((ic)object).b(hm3);
            ki3.a(hm3);
        }
        hm2.a("TileEntities", ki3);
    }

    public static ga a(cn cn2, hm hm2) {
        ki ki2;
        Object object;
        ki ki3;
        int n2 = hm2.e("xPos");
        int n3 = hm2.e("zPos");
        ga ga2 = new ga(cn2, n2, n3);
        ga2.b = hm2.j("Blocks");
        ga2.e = new mu(hm2.j("Data"));
        ga2.f = new mu(hm2.j("SkyLight"));
        ga2.g = new mu(hm2.j("BlockLight"));
        ga2.h = hm2.j("HeightMap");
        ga2.n = hm2.m("TerrainPopulated");
        if (!ga2.e.a()) {
            ga2.e = new mu(ga2.b.length);
        }
        if (ga2.h == null || !ga2.f.a()) {
            ga2.h = new byte[256];
            ga2.f = new mu(ga2.b.length);
            ga2.c();
        }
        if (!ga2.g.a()) {
            ga2.g = new mu(ga2.b.length);
            ga2.a();
        }
        if ((ki3 = hm2.l("Entities")) != null) {
            for (int i2 = 0; i2 < ki3.c(); ++i2) {
                hm hm3 = (hm)ki3.a(i2);
                object = ew.a(hm3, cn2);
                ga2.r = true;
                if (object == null) continue;
                ga2.a((kh)object);
            }
        }
        if ((ki2 = hm2.l("TileEntities")) != null) {
            for (int i3 = 0; i3 < ki2.c(); ++i3) {
                object = (hm)ki2.a(i3);
                ic ic2 = ic.c((hm)object);
                if (ic2 == null) continue;
                ga2.a(ic2);
            }
        }
        return ga2;
    }

    public void a() {
    }

    public void b() {
    }

    public void b(cn cn2, ga ga2) {
    }
}

