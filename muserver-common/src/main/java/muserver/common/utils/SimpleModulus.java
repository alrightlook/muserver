package muserver.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class SimpleModulus {
 public static final Integer FILE_HEADER = 4370;
 public static final Integer ENCRYPTED_BLOCK_SIZE = 11;
 public static final Integer ENCRYPTION_KEY_SIZE = 4;
 public static final Integer ENCRYPTION_BLOCK_SIZE = 8;

 public static final byte[] XOR_FILTER_TABLE = new byte[]{
     (byte) 0xAB, 0x11, (byte) 0xCD, (byte) 0xFE, 0x18, 0x23, (byte) 0xC5, (byte) 0xA3, (byte) 0xCA, 0x33, (byte) 0xC1, (byte) 0xCC, 0x66, 0x67, 0x21, (byte) 0xF3, 0x32, 0x12, 0x15, 0x35, 0x29, (byte) 0xFF, (byte) 0xFE, 0x1D, 0x44, (byte) 0xEF, (byte) 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D,
 };

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

 public static int encrypt(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  int iTempSize = iSize;
  int iTempSize2;
  int iOriSize;
  ByteBuffer lpTempDest = lpDest;
  ByteBuffer lpTempSource = lpSource;
  int iDec = ((iSize + 7) / 8);
  iSize = (iDec + iDec * 4) * 2 + iDec;
  if (lpDest != null) {
   iOriSize = iTempSize;
   for (int i = 0; i < iTempSize; i += 8, iOriSize -= 8, lpTempDest.position(lpTempDest.position() + 11)) {
    iTempSize2 = iOriSize;
    if (iOriSize >= 8) {
     iTempSize2 = 8;
    }
    encryptBlock(lpTempDest, (ByteBuffer) lpTempSource.position(lpTempSource.position() + i), iTempSize2);
   }
  }

  return iSize;
 }

 public static int encryptBlock(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  ByteBuffer dwEncBuffer = ByteBuffer.allocate(Integer.BYTES * 4);
  ByteBuffer dwEncValue = ByteBuffer.allocate(4); // TempVar1
  dwEncBuffer.putInt(0);

  ByteBuffer lpEncDest = lpDest;
  ByteBuffer lpEncSource = lpSource;

  memset(lpEncDest, 0, 11);

  for (int i = 0; i < 4; i++) {
   int val = (XOR_KEY_TABLE[i] ^ (lpEncSource.getShort(i) ^ dwEncValue.getInt(0)) * ENCRYPTION_KEY_TABLE[i]) % MODULUS_KEY_TABLE[i];
   dwEncBuffer.putInt(i, val);
   dwEncValue = ByteBuffer.allocate(4).putInt(dwEncBuffer.getInt(i) & 0xFFFF);
  }

  for (int i = 0; i < 3; i++) {
   dwEncBuffer.putInt(i, dwEncBuffer.get(i) ^ XOR_KEY_TABLE[i] ^ (dwEncBuffer.get(i + 1) & 0xFFFF));
  }

  int iBitPos = 0;

  for (int i = 0; i < 4; i++) {
   iBitPos = addBits(lpDest, iBitPos, (ByteBuffer) dwEncBuffer.position(i), 0, 16);
   iBitPos = addBits(lpDest, iBitPos, (ByteBuffer) dwEncBuffer.position(i), 22, 2);
  }

  byte btCheckSum = (byte) 0xF8;

  for (int i = 0; i < 8; i++) {
   btCheckSum ^= lpEncSource.get(i);
  }

  dwEncValue.put(1, btCheckSum);
  dwEncValue.put(0, (byte) (btCheckSum ^ iSize ^ 0x3D));

  return addBits(lpDest, iBitPos, dwEncValue, 0, 16);
 }

 public static int decrypt(ByteBuffer lpTarget, ByteBuffer lpSource, int size) {
  int result = (size * 8) / 11;
  int decSize = 0;

  ByteBuffer lpTempDest = lpTarget;
  ByteBuffer lpTempSrc = lpSource;

  if (size > 0) {
   while (decSize < size) {
    int tempResult = decryptBlock(lpTempDest, lpTempSrc);

    if (result < 0) {
     return result;
    }

    result += tempResult;
    decSize += 11;
    lpTempSrc.position(lpTempSrc.position() + 11);
    lpTempDest.position(lpTempDest.position() + 8);

   }
  }

  return result;
 }

 public static int decryptBlock(ByteBuffer lpTarget, ByteBuffer lpSource) {
  ByteBuffer decBuffer = ByteBuffer.allocate(Integer.BYTES * 4);

  ByteBuffer lpTempSource = lpSource;
  ByteBuffer lpTempTarget = lpTarget;

  int bitPos = 0;

  for (int n = 0; n < 4; n++) {
   addBits(decBuffer, 0, lpTempSource, bitPos, 16);
   bitPos += 16;
   addBits(decBuffer, 22, lpTempSource, bitPos, 2);
   bitPos += 2;
  }

  for (int n = 2; n >= 0; n--) {
   int val = (decBuffer.getInt(n) ^ XOR_KEY_TABLE[n]) ^ decBuffer.getShort(n + 1);
   decBuffer.putInt(n, val);
  }

  Integer value = 0;

  for (int n = 0; n < 4; n++) {
   lpTempTarget.putShort(n, (short) (((DECRYPTION_KEY_TABLE[n] * decBuffer.getInt(n)) % (MODULUS_KEY_TABLE[n]) ^ XOR_KEY_TABLE[n]) ^ value));
   value = decBuffer.getInt(n);
  }

  decBuffer.putInt(0, 0);
  addBits(decBuffer, 0, lpTempSource, bitPos, 16);
  decBuffer.put(0, (byte) ((decBuffer.get(0) ^ decBuffer.get(1)) ^ 0x3D));

  byte btCheckSum = (byte) 0xF8;

  for (int n = 0; n < 8; n++) {
   btCheckSum ^= lpTempTarget.get(n);
  }

  if (btCheckSum != decBuffer.get(1)) {
   return -1;
  }

  return decBuffer.get(0);
 }

 public static int addBits(ByteBuffer lpTarget, int targetBitPos, ByteBuffer lpSource, int sourceBitPos, int size) {
  int sourceBitSize = sourceBitPos + size;
  int tempSize1 = getByteOfBit(sourceBitSize - 1) + (1 - getByteOfBit(sourceBitPos));

  ByteBuffer lpTempBuff = ByteBuffer.allocate(tempSize1 + 1);
  byte lpSourceByteOfBit = getByteOfBit(sourceBitPos);
  memcpy(lpTempBuff, (ByteBuffer) lpSource.position(lpSource.position() + lpSourceByteOfBit), tempSize1);

  if ((sourceBitSize % 8) != 0) {
   byte val = lpTempBuff.get(tempSize1 - 1);
   val &= 255 << (8 - (sourceBitSize % 8));
   lpTempBuff.put(tempSize1 - 1, val);
  }

  int iShiftLeft = (sourceBitPos % 8);
  int iShiftRight = (targetBitPos % 8);

  shift(lpTempBuff, tempSize1, -iShiftLeft);
  shift(lpTempBuff, tempSize1 + 1, iShiftRight);

  int iNewTempBufferLen = ((iShiftRight <= iShiftLeft) ? 0 : 1) + tempSize1;
  byte lpDestByteOfBit = getByteOfBit(targetBitPos);
  ByteBuffer TempDist = (ByteBuffer) lpTarget.position(lpDestByteOfBit);

  for (int i = 0; i < iNewTempBufferLen; i++) {
   byte val = lpTempBuff.get(i);
   val |= lpTempBuff.get(i);
   TempDist.put(i, val);
  }

  // Delete the temp Buffer
  lpTempBuff = null;

  // Return the number of bits of the new Dest Buffer
  return targetBitPos + size;
 }

 public static byte getByteOfBit(int btByte) {
  byte val = (byte) (btByte >> 3);
  return val;
 }

 public static void shift(ByteBuffer lpBuff, int iSize, int ShiftLen) {
  ByteBuffer TempBuff = lpBuff;

  // Case no Shift Len
  if (ShiftLen != 0) {
   // Shift Right
   if (ShiftLen > 0) {
    if ((iSize - 1) > 0) {
     for (int i = (iSize - 1); i > 0; i--) {
      TempBuff.put(i, (byte) ((TempBuff.get(i - 1) << ((8 - ShiftLen))) | (TempBuff.get(i) >>> ShiftLen)));
     }
    }

    byte val = TempBuff.get(0);
    val >>>= ShiftLen;
    TempBuff.put(0, val);
   } else // Shift Left
   {
    ShiftLen = -ShiftLen;

    if ((iSize - 1) > 0) {
     for (int i = 0; i < (iSize - 1); i++) {
      TempBuff.put(i, (byte) ((TempBuff.get(i + 1) >>> ((8 - ShiftLen))) | (TempBuff.get(i) << ShiftLen)));
     }
    }

    byte val = TempBuff.get(iSize - 1);
    val <<= ShiftLen;
    TempBuff.put(iSize - 1, val);
   }
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
   val ^= buffer.get(i - 1) ^ XOR_FILTER_TABLE[i % 32];
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

  int fileLength = EndianUtils.readShortLE(stream);

  if (fileLength != fileBuf.length) {
   throw new Exception("Invalid file length");
  }

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

 private static void memcpy(ByteBuffer pTempBuffer, ByteBuffer pSourceBuffer, int iTempBufferLen) {
  ByteBuffer src = pSourceBuffer.duplicate();
  src.limit(pSourceBuffer.position() + iTempBufferLen);
  pTempBuffer.put(src);
 }

 private static void memset(ByteBuffer pTempBuffer, int offset, int size) {
  pTempBuffer.mark();
  for (int i = offset; i < size; i++) {
   pTempBuffer.put((byte) 0);
  }
  pTempBuffer.reset();
 }
}