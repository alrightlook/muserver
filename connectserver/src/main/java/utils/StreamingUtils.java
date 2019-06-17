package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamingUtils {
    public static void writeByte(byte value, OutputStream stream) throws IOException {
        stream.write(value);
    }

    public static void writeInt(int value, OutputStream stream) throws IOException {
        writeByte((byte) (value & 0xFF), stream);
        writeByte((byte) ((value >> 8) & 0xFF), stream);
        writeByte((byte) ((value >> 16) & 0xFF), stream);
        writeByte((byte) ((value >> 24) & 0xFF), stream);
    }

    public static void writeLong(long value, OutputStream stream) throws IOException {
        writeByte((byte) (value & 0xFF), stream);
        writeByte((byte) ((value >> 8) & 0xFF), stream);
        writeByte((byte) ((value >> 16) & 0xFF), stream);
        writeByte((byte) ((value >> 24) & 0xFF), stream);
        writeByte((byte) ((value >> 32) & 0xFF), stream);
        writeByte((byte) ((value >> 40) & 0xFF), stream);
        writeByte((byte) ((value >> 48) & 0xFF), stream);
        writeByte((byte) ((value >> 56) & 0xFF), stream);
    }

    public static void writeDouble(double value, OutputStream stream) throws IOException {
        writeLong(Double.doubleToLongBits(value), stream);
    }

    public static void writeString(String value, OutputStream stream) throws IOException {
        stream.write(value.getBytes());
    }

    public static void writeBytes(byte[] data, OutputStream stream) throws IOException {
        stream.write(data);
    }

    public static void writeBytes(byte[] data, int offset, int length, OutputStream stream) throws IOException {
        stream.write(data, offset, length);
    }

    public static int readInt(InputStream stream) throws IOException {
        int val1 = stream.read();
        int val2 = stream.read();
        int val3 = stream.read();
        int val4 = stream.read();
        return val1 + (val2 << 8) + (val3 << 16) + (val4 << 24);
    }

    public static long readUInt(InputStream stream) throws IOException {
        int val1 = stream.read();
        int val2 = stream.read();
        int val3 = stream.read();
        int val4 = stream.read();
        return val1 + (val2 << 8) + (val3 << 16) + (val4 << 24);
    }

    public static long readLong(InputStream stream) throws IOException {
        long val1 = readUInt(stream);
        long val2 = readUInt(stream);
        return val1 + (val2 << 32);
    }

    public static double readDouble(InputStream stream) throws IOException {
        return Double.longBitsToDouble(readLong(stream));
    }

    public static String readString(int count, InputStream stream) throws IOException {
        byte[] raw = readBytes(count, stream);
        return new String(raw);
    }

    public static byte[] readBytes(int count, InputStream stream) throws IOException {
        byte[] buffer = new byte[count];
        int offset = 0;
        while (offset < buffer.length) {
            int read = stream.read(buffer, offset, buffer.length - offset);
            if (read > 0) {
                offset += read;
            } else if (read < 0) {
                throw new IOException();
            } else {
                Thread.yield();
            }
        }
        return buffer;
    }
}