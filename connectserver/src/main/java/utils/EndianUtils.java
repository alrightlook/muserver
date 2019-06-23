package utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {
 public static void writeByte(byte[] data, int offset, byte value) {
  data[offset + 0] = (byte) (value >> 0 & 0xFF);
 }

 public static void writeByte(OutputStream output, byte value) throws IOException {
  output.write((byte) (value >> 0 & 0xFF));
 }


 public static void writeShort(byte[] data, int offset, short value) {
  data[offset + 0] = (byte) (value >> 0 & 0xFF);
  data[offset + 1] = (byte) (value >> 8 & 0xFF);
 }

 public static void writeShort(OutputStream output, short value) throws IOException {
  output.write((byte) (value >> 0 & 0xFF));
  output.write((byte) (value >> 8 & 0xFF));
 }


 public static void writeInteger(byte[] data, int offset, int value) {
  data[offset + 0] = (byte) (value >> 0 & 0xFF);
  data[offset + 1] = (byte) (value >> 8 & 0xFF);
  data[offset + 2] = (byte) (value >> 16 & 0xFF);
  data[offset + 3] = (byte) (value >> 24 & 0xFF);
 }

 public static void writeInteger(OutputStream output, int value) throws IOException {
  output.write((byte) (value >> 0 & 0xFF));
  output.write((byte) (value >> 8 & 0xFF));
  output.write((byte) (value >> 16 & 0xFF));
  output.write((byte) (value >> 24 & 0xFF));
 }


 public static void writeLong(byte[] data, int offset, long value) {
  data[offset + 0] = (byte) ((int) (value >> 0 & 0xFF));
  data[offset + 1] = (byte) ((int) (value >> 8 & 0xFF));
  data[offset + 2] = (byte) ((int) (value >> 16 & 0xFF));
  data[offset + 3] = (byte) ((int) (value >> 24 & 0xFF));
  data[offset + 4] = (byte) ((int) (value >> 32 & 0xFF));
  data[offset + 5] = (byte) ((int) (value >> 40 & 0xFF));
  data[offset + 6] = (byte) ((int) (value >> 48 & 0xFF));
  data[offset + 7] = (byte) ((int) (value >> 56 & 0xFF));
 }

 public static void writeLong(OutputStream output, long value) throws IOException {
  output.write((byte) ((int) (value >> 0 & 0xFF)));
  output.write((byte) ((int) (value >> 8 & 0xFF)));
  output.write((byte) ((int) (value >> 16 & 0xFF)));
  output.write((byte) ((int) (value >> 24 & 0xFF)));
  output.write((byte) ((int) (value >> 32 & 0xFF)));
  output.write((byte) ((int) (value >> 40 & 0xFF)));
  output.write((byte) ((int) (value >> 48 & 0xFF)));
  output.write((byte) ((int) (value >> 56 & 0xFF)));
 }


 public static void writeFloat(byte[] data, int offset, float value) {
  writeInteger(data, offset, Float.floatToIntBits(value));
 }

 public static void writeFloat(OutputStream output, float value) throws IOException {
  writeInteger(output, Float.floatToIntBits(value));
 }


 public static void writeDouble(byte[] data, int offset, double value) {
  writeLong(data, offset, Double.doubleToLongBits(value));
 }

 public static void writeDouble(OutputStream output, double value) throws IOException {
  writeLong(output, Double.doubleToLongBits(value));
 }


 public static byte readByte(byte[] data, int offset) {
  return (byte) ((data[offset + 0] & 0xFF) << 0);
 }

 public static byte readByte(InputStream input) throws IOException {
  return (byte) ((read(input) & 0xFF) << 0);
 }


 public static short readShort(byte[] data, int offset) {
  return (short) (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8));
 }

 public static int readUnsignedShort(byte[] data, int offset) {
  return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8);
 }

 public static short readShort(InputStream input) throws IOException {
  return (short) (((read(input) & 0xFF) << 0) + ((read(input) & 0xFF) << 8));
 }

 public static int readUnsignedShort(InputStream input) throws IOException {
  int value1 = read(input);
  int value2 = read(input);
  return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8);
 }


 public static int readInteger(byte[] data, int offset) {
  return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
 }

 public static long readUnsignedInteger(byte[] data, int offset) {
  long low = (long) (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16));
  long high = (long) (data[offset + 3] & 0xFF);
  return (high << 24) + (4294967295L & low);
 }

 public static int readInteger(InputStream input) throws IOException {
  int value1 = read(input);
  int value2 = read(input);
  int value3 = read(input);
  int value4 = read(input);
  return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16) + ((value4 & 0xFF) << 24);
 }

 public static long readUnsignedInteger(InputStream input) throws IOException {
  int value1 = read(input);
  int value2 = read(input);
  int value3 = read(input);
  int value4 = read(input);
  long low = (long) (((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16));
  long high = (long) (value4 & 0xFF);
  return (high << 24) + (4294967295L & low);
 }


 public static long readLong(byte[] data, int offset) {
  long low = (long) readInteger(data, offset);
  long high = (long) readInteger(data, offset + 4);
  return (high << 32) + (4294967295L & low);
 }

 public static long readLong(InputStream input) throws IOException {
  byte[] bytes = new byte[8];

  for (int i = 0; i < 8; ++i) {
   bytes[i] = (byte) read(input);
  }

  return readLong(bytes, 0);
 }


 public static float readFloat(byte[] data, int offset) {
  return Float.intBitsToFloat(readInteger(data, offset));
 }

 public static float readFloat(InputStream input) throws IOException {
  return Float.intBitsToFloat(readInteger(input));
 }


 public static double readDouble(byte[] data, int offset) {
  return Double.longBitsToDouble(readLong(data, offset));
 }

 public static double readDouble(InputStream input) throws IOException {
  return Double.longBitsToDouble(readLong(input));
 }


 private static int read(InputStream input) throws IOException {
  int value = input.read();
  if (value == -1) {
   throw new EOFException("Unexpected EOF reached");
  } else {
   return value;
  }
 }
}