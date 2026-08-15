/*
 * Decompiled with CFR 0.152.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class x {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static hm a(InputStream inputStream) {
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(inputStream));
        try {
            hm hm2 = x.a(dataInputStream);
            return hm2;
        }
        finally {
            dataInputStream.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void a(hm hm2, OutputStream outputStream) {
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(outputStream));
        try {
            x.a(hm2, dataOutputStream);
        }
        finally {
            dataOutputStream.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static hm a(byte[] byArray) {
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(byArray)));
        try {
            hm hm2 = x.a(dataInputStream);
            return hm2;
        }
        finally {
            dataInputStream.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] a(hm hm2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(byteArrayOutputStream));
        try {
            x.a(hm2, dataOutputStream);
        }
        finally {
            dataOutputStream.close();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static hm a(DataInput dataInput) {
        el el2 = el.b(dataInput);
        if (el2 instanceof hm) {
            return (hm)el2;
        }
        throw new IOException("Root tag must be a named compound tag");
    }

    public static void a(hm hm2, DataOutput dataOutput) {
        el.a(hm2, dataOutput);
    }
}

