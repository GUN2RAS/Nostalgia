/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInput;
import java.io.DataOutput;

public abstract class el {
    private String a = null;

    abstract void a(DataOutput var1);

    abstract void a(DataInput var1);

    public abstract byte a();

    public String b() {
        if (this.a == null) {
            return "";
        }
        return this.a;
    }

    public el a(String string) {
        this.a = string;
        return this;
    }

    public static el b(DataInput dataInput) {
        byte by2 = dataInput.readByte();
        if (by2 == 0) {
            return new fy();
        }
        el el2 = el.a(by2);
        el2.a = dataInput.readUTF();
        el2.a(dataInput);
        return el2;
    }

    public static void a(el el2, DataOutput dataOutput) {
        dataOutput.writeByte(el2.a());
        if (el2.a() == 0) {
            return;
        }
        dataOutput.writeUTF(el2.b());
        el2.a(dataOutput);
    }

    public static el a(byte by2) {
        switch (by2) {
            case 0: {
                return new fy();
            }
            case 1: {
                return new ix();
            }
            case 2: {
                return new ls();
            }
            case 3: {
                return new io();
            }
            case 4: {
                return new gn();
            }
            case 5: {
                return new f();
            }
            case 6: {
                return new kr();
            }
            case 7: {
                return new dy();
            }
            case 8: {
                return new ne();
            }
            case 9: {
                return new ki();
            }
            case 10: {
                return new hm();
            }
        }
        return null;
    }

    public static String b(byte by2) {
        switch (by2) {
            case 0: {
                return "TAG_End";
            }
            case 1: {
                return "TAG_Byte";
            }
            case 2: {
                return "TAG_Short";
            }
            case 3: {
                return "TAG_Int";
            }
            case 4: {
                return "TAG_Long";
            }
            case 5: {
                return "TAG_Float";
            }
            case 6: {
                return "TAG_Double";
            }
            case 7: {
                return "TAG_Byte_Array";
            }
            case 8: {
                return "TAG_String";
            }
            case 9: {
                return "TAG_List";
            }
            case 10: {
                return "TAG_Compound";
            }
        }
        return "UNKNOWN";
    }
}

