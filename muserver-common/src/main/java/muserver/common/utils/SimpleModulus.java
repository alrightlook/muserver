package muserver.common.utils;

import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.Arrays;

public class SimpleModulus {
 public static final Integer FILE_HEADER = 4370;
 public static final Integer ENCRYPTED_BLOCK_SIZE = 11;
 public static final Integer ENCRYPTION_KEY_SIZE = 4;
 public static final Integer ENCRYPTION_BLOCK_SIZE = 8;

 public static final byte[] XOR_FILTER_TABLE = new byte[]{
     (byte) 0xAB, 0x11, (byte) 0xCD, (byte) 0xFE, 0x18, 0x23, (byte) 0xC5, (byte) 0xA3, (byte) 0xCA, 0x33, (byte) 0xC1, (byte) 0xCC, 0x66, 0x67, 0x21, (byte) 0xF3, 0x32, 0x12, 0x15, 0x35, 0x29, (byte) 0xFF, (byte) 0xFE, 0x1D, 0x44, (byte) 0xEF, (byte) 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D,
 };

 public static final Integer[] XOR_TABLE = {
     0x3F08A79B,
     0xE25CC287,
     0x93D27AB9,
     0x20DEA7BF
 };

 public static final Integer[] XOR_KEY_TABLE = {
     0xBD1D,
     0xB455,
     0x3B43,
     0x9239
 };

 public static final Integer[] MODULUS_KEY_TABLE = {
     0x1F44F,
     0x28386,
     0x1125B,
     0x1A192
 };

 public static final Integer[] DECRYPTION_KEY_TABLE = {
     0x7B38,
     0x7FF,
     0xDEB3,
     0x27C7,
 };

 public static final byte[] XOR_FILTER = new byte[]{
         (byte) 0xAB, 0x11, (byte) 0xCD, (byte) 0xFE, 0x18, 0x23, (byte) 0xC5, (byte) 0xA3, (byte) 0xCA, 0x33, (byte) 0xC1, (byte) 0xCC, 0x66, 0x67, 0x21, (byte) 0xF3, 0x32, 0x12, 0x15, 0x35, 0x29, (byte) 0xFF, (byte) 0xFE, 0x1D, 0x44, (byte) 0xEF, (byte) 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D,
 };

 public static int encrypt(byte[] lpDest, byte[] lpSource, int iSize) {
  throw new UnsupportedOperationException();
 }

 public static int encryptBlock(Integer[] lpDest, byte[] lpSource, int iSize) {
  throw new UnsupportedOperationException();
 }

 public static int decrypt(ByteBuf lpDest, ByteBuf lpSource, int iSize) throws IOException {
  if (lpDest == null) {
   return (iSize * 8) / 11;
  }

  int result = 0;
  int decLen = 0;

  if (iSize > 0) {
   while (decLen < iSize) {
    int tempResult = decryptBlock(lpDest, lpSource);

    if (result < 0) {
     return result;
    }

    result += tempResult;
    decLen += 11;
    lpDest = lpDest.slice(8, lpDest.readableBytes() - 8);
    lpSource = lpSource.slice(11, lpSource.readableBytes() - 11);
   }
  }

  return result;
 }

 public static int decryptBlock(ByteBuf lpDest, ByteBuf lpSource) throws IOException {
  lpDest.setZero(0, 8);
  ByteBuf dwDecBuffer = Unpooled.buffer(Integer.BYTES * 4);
  int iBitPosition = 0;
  for (int i = 0; i < 4; i++) {
   addBits(dwDecBuffer.slice(i * 4, 4), 0, lpSource, iBitPosition, 16);
   iBitPosition += 16;
   addBits(dwDecBuffer.slice(i * 4, 4), 22, lpSource, iBitPosition, 2);
   iBitPosition += 2;
  }

  //todo: Re-order dwDecBuffer for correct results

  for (int i = 2; i >= 0; i--) {
   int position = i * 4;
   long val = (dwDecBuffer.getUnsignedIntLE(position) ^ XOR_KEY_TABLE[i]) ^ dwDecBuffer.getUnsignedShortLE(position + 4);
   dwDecBuffer.setInt(position, (int) val);
  }

  Integer temp = 0, temp1;

  for (int i = 0; i < 4; i++) {
   temp1 = ((DECRYPTION_KEY_TABLE[i] * (dwDecBuffer.getInt(i))) % (MODULUS_KEY_TABLE[i])) ^ XOR_KEY_TABLE[i] ^ temp;
   temp = dwDecBuffer.getInt(i) & 0xFFFF;
   lpDest.setShort(i, temp1);
  }


  dwDecBuffer.setZero(0, 4);
  addBits(dwDecBuffer, 0, lpSource, iBitPosition, 16);
  dwDecBuffer.setByte(0, (byte) (dwDecBuffer.getByte(1) ^ dwDecBuffer.getByte(0) ^ 0x3D));

  byte btCheckSum = (byte) 0xF8;

  for (int i = 0; i < 8; i++) {
   btCheckSum ^= lpDest.getByte(i);
  }

  if (btCheckSum != dwDecBuffer.getByte(1)) {
   return -1;
  }

  return dwDecBuffer.getByte(0);
 }

 public static int addBits(ByteBuf lpDest, int iDestBitPos, ByteBuf lpSource, int iBitSourcePos, int iBitLen) {
  int iSourceBufferBitLen = iBitSourcePos + iBitLen;
  int iTempBufferLen = getByteOfBit(iSourceBufferBitLen - 1) + (1 - getByteOfBit(iBitSourcePos));

  ByteBuf pTempBuffer = Unpooled.buffer(iTempBufferLen + 1);
  pTempBuffer.setZero(0, iTempBufferLen + 1);
  pTempBuffer.writeBytes(memcpy(lpSource, iBitSourcePos, iTempBufferLen));

  if ((iSourceBufferBitLen % 8) != 0) {
   byte val = (byte) (pTempBuffer.getUnsignedByte(iTempBufferLen - 1) & 0xFF << (8 - (iSourceBufferBitLen % 8)));
   pTempBuffer.setByte(iTempBufferLen - 1, val);
  }

  int iShiftLeft = (iBitSourcePos % 8);
  int iShiftRight = (iDestBitPos % 8);

  shift(pTempBuffer, iTempBufferLen, -iShiftLeft);
  shift(pTempBuffer, iTempBufferLen + 1, iShiftRight);

  int iNewTempBufferLen = ((iShiftRight <= iShiftLeft) ? 0 : 1) + iTempBufferLen;
  ByteBuf tempDist = lpDest.slice(getByteOfBit(iDestBitPos), iNewTempBufferLen);

  for (int i = 0; i < iNewTempBufferLen; i++) {
   byte val = (byte) (tempDist.getUnsignedByte(i) | pTempBuffer.getUnsignedByte(i));
   tempDist.setByte(i, val);
  }

  return iDestBitPos + iBitLen;
 }

 public static byte getByteOfBit(int btByte) {
  byte val = (byte) (btByte >> 3);
  return val;
 }

 public static void shift(ByteBuf lpBuff, int iSize, int shiftLen) {
  if (shiftLen != 0) {
   if (shiftLen > 0) {
    if ((iSize - 1) > 0) {
     for (int i = (iSize - 1); i > 0; i--) {
      byte val = (byte) ((lpBuff.getByte(i - 1) << ((8 - shiftLen))) | (lpBuff.getByte(i) >> shiftLen));
      lpBuff.setByte(i, val);
     }
    }
    byte val = (byte) (lpBuff.getByte(0) >> shiftLen);
    lpBuff.setByte(0, val);
   } else {
    shiftLen = -shiftLen;

    if ((iSize - 1) > 0) {
     for (int i = 0; i < (iSize - 1); i++) {
      int val = (lpBuff.getUnsignedByte(i + 1) >> (8 - shiftLen));
      val |= (lpBuff.getUnsignedByte(i) << shiftLen);
      lpBuff.setByte(i, val);
     }
    }

    byte val = (byte) (lpBuff.getByte(iSize - 1) << shiftLen);
    lpBuff.setByte(iSize - 1, val);
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
   System.out.println(String.format("%X", XOR_TABLE[n] ^ modulusKeys[n]));
  }

  System.out.println("Encryption keys");

  Integer[] encryptionKeys = new Integer[]{EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_TABLE[n] ^ encryptionKeys[n]));
  }

  System.out.println("Decryption keys");

  Integer[] decryptionKeys = new Integer[]{EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream), EndianUtils.readIntegerLE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_TABLE[n] ^ decryptionKeys[n]));
  }

  return true;
 }

 private static ByteBuf memcpy(ByteBuf source, int offset, int size) {
  return source.copy(getByteOfBit(offset), size);
 }
}