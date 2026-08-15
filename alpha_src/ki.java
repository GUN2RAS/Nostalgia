/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInput;
import java.io.DataOutput;
import java.util.ArrayList;
import java.util.List;

public class ki
extends el {
    private List a = new ArrayList();
    private byte b;

    void a(DataOutput dataOutput) {
        this.b = this.a.size() > 0 ? ((el)this.a.get(0)).a() : (byte)1;
        dataOutput.writeByte(this.b);
        dataOutput.writeInt(this.a.size());
        for (int i2 = 0; i2 < this.a.size(); ++i2) {
            ((el)this.a.get(i2)).a(dataOutput);
        }
    }

    void a(DataInput dataInput) {
        this.b = dataInput.readByte();
        int n2 = dataInput.readInt();
        this.a = new ArrayList();
        for (int i2 = 0; i2 < n2; ++i2) {
            el el2 = el.a(this.b);
            el2.a(dataInput);
            this.a.add(el2);
        }
    }

    public byte a() {
        return 9;
    }

    public String toString() {
        return "" + this.a.size() + " entries of type " + el.b(this.b);
    }

    public void a(el el2) {
        this.b = el2.a();
        this.a.add(el2);
    }

    public el a(int n2) {
        return (el)this.a.get(n2);
    }

    public int c() {
        return this.a.size();
    }
}

