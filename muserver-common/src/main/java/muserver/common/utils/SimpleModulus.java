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

 public static int decrypt(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  if (lpDest == null) {
   return iSize * 8 / 11;
  }

  ByteBuffer lpTempDest = lpDest;
  ByteBuffer lpTempSrc = lpSource;

  int iResult = 0;
  int iDecLen = 0;

  if (iSize > 0) {
   while (iDecLen < iSize) {
    int iTempResult = decryptBlock(lpTempDest, lpTempSrc);

    if (iResult < 0) {
     return iResult;
    }

    iResult += iTempResult;
    iDecLen += 11;
    lpTempSrc.position(lpTempSrc.position() + 11);
    lpTempDest.position(lpTempDest.position() + 8);

   }
  }

  return iResult;
 }

 public static int decryptBlock(ByteBuffer lpDest, ByteBuffer lpSource) {
  ByteBuffer dwDecBuffer = ByteBuffer.allocate(Integer.BYTES * 4);
  int iBitPosition = 0;

  ByteBuffer lpDecDest = lpDest;
  ByteBuffer lpDecSource = lpSource;

  for (int i = 0; i < 4; i++) {
   addBits(dwDecBuffer, 0, lpDecSource, iBitPosition, 16);
   dwDecBuffer.position(dwDecBuffer.position() + i * 4);
   iBitPosition += 16;
   addBits(dwDecBuffer, 22, lpDecSource, iBitPosition, 2);
   dwDecBuffer.position(dwDecBuffer.position() + i * 4);
   iBitPosition += 2;
  }

  for (int i = 2; i >= 0; i--) {
   byte val = (byte) (dwDecBuffer.get(i) ^ XOR_KEY_TABLE[i] ^ (dwDecBuffer.get(i + 1) & 0xFFFF));
   dwDecBuffer.put(i, val);
  }

  int Temp = 0, Temp1;

  for (int i = 0; i < 4; i++) {
   Temp1 = ((DECRYPTION_KEY_TABLE[i] * (dwDecBuffer.get(i))) % (MODULUS_KEY_TABLE[i])) ^ XOR_KEY_TABLE[i] ^ Temp;
   Temp = dwDecBuffer.get(i) & 0xFFFF;
   lpDecDest.putShort(i, (short) Temp1);
  }

  dwDecBuffer.put(0, (byte) 0);
  addBits(dwDecBuffer, 0, lpDecSource, iBitPosition, 16);
  dwDecBuffer.put(0, (byte) (dwDecBuffer.get(1) ^ dwDecBuffer.get(0) ^ 0x3D));

  byte btCheckSum = (byte) 0xF8;

  for (int i = 0; i < 8; i++) {
   btCheckSum ^= lpDecDest.get(i);
  }

  if (btCheckSum != dwDecBuffer.get(1)) {
   return -1;
  }

  return dwDecBuffer.get(0);
 }

 public static int addBits(ByteBuffer lpDest, int iDestBitPos, ByteBuffer lpSource, int iBitSourcePos, int iBitLen) {
  int iSourceBufferBitLen = iBitLen + iBitSourcePos;
  int iTempBufferLen = getByteOfBit(iSourceBufferBitLen - 1);
  iTempBufferLen += 1 - getByteOfBit(iBitSourcePos);

  ByteBuffer pTempBuffer = ByteBuffer.allocate(iTempBufferLen + 1);
  byte lpSourceByteOfBit = getByteOfBit(iBitSourcePos);
  memcpy(pTempBuffer, (ByteBuffer) lpSource.position(lpSource.position() + lpSourceByteOfBit), iTempBufferLen);

  if ((iSourceBufferBitLen % 8) != 0) {
   byte val = pTempBuffer.get(iTempBufferLen - 1);
   val &= 255 << (8 - (iSourceBufferBitLen % 8));
   pTempBuffer.put(iTempBufferLen - 1, val);
  }

  // Get the Values to Shift
  int iShiftLeft = (iBitSourcePos % 8);
  int iShiftRight = (iDestBitPos % 8);

  // Shift the Values to Add the right space of the desired bits
  shift(pTempBuffer, iTempBufferLen, -iShiftLeft);
  shift(pTempBuffer, iTempBufferLen + 1, iShiftRight);

  // Copy the the bits of Source to the Dest
  int iNewTempBufferLen = ((iShiftRight <= iShiftLeft) ? 0 : 1) + iTempBufferLen;
  byte lpDestByteOfBit = getByteOfBit(iDestBitPos);
  ByteBuffer TempDist = (ByteBuffer) lpDest.position(lpDestByteOfBit);

  for (int i = 0; i < iNewTempBufferLen; i++) {
   byte val = pTempBuffer.get(i);
   val |= pTempBuffer.get(i);
   TempDist.put(i, val);
  }

  // Delete the temp Buffer
  pTempBuffer = null;

  // Return the number of bits of the new Dest Buffer
  return iDestBitPos + iBitLen;
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
  for (int i = offset; i < size; i++) {
   pTempBuffer.put((byte) 0);
  }
 }
}