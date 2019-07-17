package muserver.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

public class SimpleModulus {
 public static final Integer FILE_HEADER = 4370;
 public static final Integer ENCRYPTED_BLOCK_SIZE = 11;
 public static final Integer ENCRYPTION_KEY_SIZE = 4;
 public static final Integer ENCRYPTION_BLOCK_SIZE = 8;

 public static final Integer[] XOR_KEY_TABLE = {
         0x3F08A79B,
         0xE25CC287,
         0x93D27AB9,
         0x20DEA7BF
 };

 public static final Integer[] MODULUS_KEY_TABLE = {
         0x1F44F,
         0x28386,
         0x1125B,
         0x1A192
 };

 public static final Integer[] ENCRYPTION_KEY_TABLE = {
         0x7B38,
         0x7FF,
         0xDEB3,
         0x27C7,
 };

 public static final Integer[] DECRYPTION_KEY_TABLE = {
         0xBD1D,
         0xB455,
         0x3B43,
         0x9239
 };
 public static final byte[] XOR_FILTER = new byte[]{
         (byte) 0xAB, 0x11, (byte) 0xCD, (byte) 0xFE, 0x18, 0x23, (byte) 0xC5, (byte) 0xA3, (byte) 0xCA, 0x33, (byte) 0xC1, (byte) 0xCC, 0x66, 0x67, 0x21, (byte) 0xF3, 0x32, 0x12, 0x15, 0x35, 0x29, (byte) 0xFF, (byte) 0xFE, 0x1D, 0x44, (byte) 0xEF, (byte) 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D,
 };

 public static final byte[] XOR_FILTER = new byte[]{
         (byte) 0xAB, 0x11, (byte) 0xCD, (byte) 0xFE, 0x18, 0x23, (byte) 0xC5, (byte) 0xA3, (byte) 0xCA, 0x33, (byte) 0xC1, (byte) 0xCC, 0x66, 0x67, 0x21, (byte) 0xF3, 0x32, 0x12, 0x15, 0x35, 0x29, (byte) 0xFF, (byte) 0xFE, 0x1D, 0x44, (byte) 0xEF, (byte) 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D,
 };

 public static int encrypt(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  throw new UnsupportedOperationException();
 }

 public static int encryptBlock(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  throw new UnsupportedOperationException();
 }

 public static int decrypt(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  if (lpDest == null) {
   return ((iSize + ENCRYPTED_BLOCK_SIZE - 1) / ENCRYPTION_BLOCK_SIZE) * ENCRYPTED_BLOCK_SIZE;
  }
  int iTotalSize = 0;
  ByteBuffer tmpSource = lpSource.duplicate(), tmpTarget = lpDest.duplicate();
  for (int i = 0; i < iSize; i += ENCRYPTED_BLOCK_SIZE, tmpSource.position(tmpSource.position() + ENCRYPTED_BLOCK_SIZE), tmpTarget.position(tmpSource.position() + ENCRYPTION_BLOCK_SIZE)) {
   int blockSize = decryptBlock(tmpTarget, tmpSource);
   if (blockSize < 0) {
    return blockSize;
   }
   iTotalSize += blockSize;
  }

  return iTotalSize;
 }

 public static int decryptBlock(ByteBuffer lpTarget, ByteBuffer lpSource) {
  ByteBuffer lpEncrypted = lpSource.duplicate();
  Integer[] dwDecBuffer = new Integer[ENCRYPTION_BLOCK_SIZE];
  lpTarget.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, 0, ENCRYPTION_BLOCK_SIZE);
  ByteBuffer lpTgt = lpTarget.duplicate();
  Arrays.fill(dwDecBuffer, dwDecBuffer.length * Integer.BYTES);
  int nTotalBits = 0;
  for (int i = 0; i < ENCRYPTION_KEY_SIZE; i++) {
  }
  throw new UnsupportedOperationException();
 }

 public static int addBits(ByteBuffer lpBuffer, Integer nNumBufferBits, ByteBuffer lpBits, Integer nInitialBit, Integer nNumBits) {
  int nBufferSize = (getByteOfBit((nNumBits + nInitialBit) - 1) - getByteOfBit(nInitialBit)) + 1;

  ByteBuffer lpTemp = ByteBuffer.allocate(nBufferSize + 1);

  for (int i = 0; i < nBufferSize + 1; i++) {
   lpTemp.put((byte) 0);
  }

  lpBits.position(getByteOfBit(nInitialBit));

  for (int i = lpBits.position(); i < nBufferSize; i++) {
   lpTemp.put(lpBits.get(i));
  }

  int nLastBitMod8 = (nNumBits + nInitialBit) % ENCRYPTION_BLOCK_SIZE;

  if (nLastBitMod8 != 0) {
   byte val = lpTemp.get(nBufferSize - 1);
   val &= (nLastBitMod8 | 0xFF) << (ENCRYPTION_BLOCK_SIZE - nLastBitMod8);
   lpTemp.put(nBufferSize - 1, val);
  }

  int nShiftLeft = (nInitialBit % ENCRYPTION_BLOCK_SIZE);
  int nShiftRight = (nNumBufferBits % ENCRYPTION_BLOCK_SIZE);

  throw new UnsupportedOperationException();
//  shift();
 }

 public static byte getByteOfBit(int btByte) {
  byte val = (byte) (btByte >> 3);
  return val;
 }

 public static void shift(ByteBuffer lpBuffer, int nByte, int nShift) {
  if (nShift > 0) {
   Buffer lpTemp = lpBuffer.position(nByte - 1);
   for (int i = nByte - 1; i > 0; i--, lpTemp.position(lpTemp.position() - 1)) {
    int position = lpTemp.position();
    lpBuffer.put(position, (byte) (lpBuffer.get(position - 1) << (ENCRYPTION_BLOCK_SIZE - nShift) | (lpBuffer.get(position) >> nShift)));
   }
   lpBuffer.put(lpTemp.position(), (byte) (lpBuffer.get(lpTemp.position()) >> nShift));
   int nRealShift = -nShift;
   for (int i = 0; i< nByte - 1; i++, lpTemp.position(lpTemp.position() + 1)) {
    int position = lpTemp.position();
    lpBuffer.put(position, (byte) (lpBuffer.get(position + 1) >> (ENCRYPTION_BLOCK_SIZE - nRealShift) | (lpBuffer.get(position) << nRealShift)));

   }
   lpBuffer.put(lpTemp.position(), (byte) (lpBuffer.get(lpTemp.position()) << nRealShift));
  }
 }

 public static void xorFilter(ByteBuffer buffer) {
  short size;
  if (buffer.get(1) == 0xC1) {
   size = buffer.get(1);
  } else {
   size = (short) (buffer.get(1) * 256 + buffer.get(2));
  }
  int start = size - 1, end = buffer.get(1) != 0xC1 ? 3 : 2;
  for (int i = start; i != end; i += -1) {
   byte val = buffer.get(i);
   val ^= buffer.get(i - 1) ^ XOR_FILTER[i % 32];
   buffer.put(i, val);
  }
 }

 public static boolean printAllKeys(File file) throws Exception {
  byte[] fileBuf = Files.readAllBytes(file.toPath());

  ByteArrayInputStream stream = new ByteArrayInputStream(fileBuf);

  short fileHeader = EndianUtils.readShortLE(stream);

  if (fileHeader != FILE_HEADER) {
   throw new Exception("Invalid file header");
  }

  short fileLength = EndianUtils.readShortLE(stream);

  if (fileLength != fileBuf.length) {
   throw new Exception("Invalid file length");
  }

  short fileUnkw3 = EndianUtils.readShortLE(stream);

  System.out.println("Modulus keys");

  Integer[] modulusKeys = new Integer[]{EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_KEY_TABLE[n] ^ modulusKeys[n]));
  }

  System.out.println("Encryption keys");

  Integer[] encryptionKeys = new Integer[]{EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_KEY_TABLE[n] ^ encryptionKeys[n]));
  }

  System.out.println("Decryption keys");

  Integer[] decryptionKeys = new Integer[]{EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_KEY_TABLE[n] ^ decryptionKeys[n]));
  }

  return true;
 }


 public static void main(String[] args) throws Exception {
  byte[] c3Packet = new byte[]{(byte) 0xC3, 0x18, 0x28, 0x6F, 0x32, 0x33, (byte) 0x90, 0xA, 0x70, 0x35, 0x51, (byte) 0xFD, (byte) 0xC8, (byte) 0xFC, 0x6D, 0x13, (byte) 0xA9, 0x15, 0x2F, (byte) 0x92, 0x0, 0x0, 0x31, 0xF};
  System.out.println(HexUtils.toString(c3Packet));

  ByteBuffer dest = ByteBuffer.allocate(c3Packet.length);
  ByteBuffer source = ByteBuffer.wrap(c3Packet);

  source.put(0, (byte) 0xC1);
//  source.put(1)
  source.position(2);


  decrypt(dest, source, c3Packet.length - 2);
  byte[] c3PacketDecrypted = dest.array();
  System.out.println(HexUtils.toString(c3PacketDecrypted));

  //Client
//  File dec2Dat = new File("/home/briankernighan/Desktop/Dec2.dat");
//  System.out.println(String.format("Print %s keys", dec2Dat.toString()));
//  if (!printAllKeys(dec2Dat)) {
//   throw new Exception("Couldn't load Dec2.dat");
//  }
//
//  File enc1Dat = new File("/home/briankernighan/Desktop/Enc1.dat");
//  System.out.println(String.format("Print %s keys", enc1Dat.toString()));
//  if (!printAllKeys(enc1Dat)) {
//   throw new Exception("Couldn't load Enc1.dat");
//  }

  //Server
//  File dec1Dat = new File("/home/victorchicu/Desktop/Dec1.dat");
//  System.out.println(String.format("Print %s keys", dec1Dat.toString()));
//  if (!printAllKeys(dec1Dat)) {
//   throw new Exception("Couldn't load Dec1.dat");
//  }
//
//  File enc2Dat = new File("/home/victorchicu/Desktop/Enc2.dat");
//  System.out.println(String.format("Print %s keys", enc2Dat.toString()));
//  if (!printAllKeys(enc2Dat)) {
//   throw new Exception("Couldn't load Enc2.dat");
 }
}