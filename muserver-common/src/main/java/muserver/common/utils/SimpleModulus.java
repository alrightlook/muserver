package muserver.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class SimpleModulus {
 public static final Integer FILE_HEADER = 4370;

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

 public static int encrypt(Byte[] lpDest, Byte[] lpSource, int iSize) {
  throw new UnsupportedOperationException();
 }

 public static int encryptBlock(Byte[] lpDest, Byte[] lpSource, int iSize) {
  throw new UnsupportedOperationException();
 }

 public static int decrypt(ByteBuffer lpDest, ByteBuffer lpSource, int iSize) {
  if (lpDest == null) {
   return iSize * 8 / 11;
  }

  int iResult = 0;
  int iDecLen = 0;


  if (iSize > 0) {
   while (iDecLen < iSize) {
    int iTempResult = decryptBlock(lpDest, lpSource);


    if (iResult < 0) {
     return iResult;
    }


    iResult += iTempResult;
    iDecLen += 11;

    int srcPos = lpSource.position();
    lpSource.position(srcPos + 11);

    int destPost = lpDest.position();
    lpDest.position(destPost + 8);
   }
  }

  return iResult;
 }

 public static int decryptBlock(ByteBuffer lpDest, ByteBuffer lpSource) {
  lpDest.clear();

  ByteBuffer dwDecBuffer = ByteBuffer.allocate(Integer.BYTES * 4);

  Integer iBitPosition = 0;

  for (int i = 0; i < 4; i++) {
   addBits(dwDecBuffer, 0, lpSource, iBitPosition, 16);
   iBitPosition += 16;
   addBits(dwDecBuffer, 22, lpSource, iBitPosition, 2);
   iBitPosition += 2;
  }


  for (int i = 2; i >= 0; i--) {
   dwDecBuffer.put(i, (byte) (dwDecBuffer.get(i) ^ XOR_KEY_TABLE[i] ^ (dwDecBuffer.get(i + 1) & 0xFFFF)));
  }

  Integer Temp = 0, Temp1;


  for (int i = 0; i < 4; i++) {
   Temp1 = ((DECRYPTION_KEY_TABLE[i] * (dwDecBuffer.get(i))) % (MODULUS_KEY_TABLE[i])) ^ XOR_KEY_TABLE[i] ^ Temp;
   Temp = dwDecBuffer.get(i) & 0xFFFF;
   lpDest.putShort(i, Temp1.shortValue());
  }

  dwDecBuffer.put(0, (byte) 0);
  addBits(dwDecBuffer, 0, lpSource, iBitPosition, 16);
  dwDecBuffer.put(0, (byte) (dwDecBuffer.get(1) ^ dwDecBuffer.get(0) ^ 0x3D));


  byte btCheckSum = (byte) 0xF8;


  for (int i = 0; i < 8; i++)
   btCheckSum ^= lpDest.get(i);


  if (btCheckSum != (dwDecBuffer.get(1)))
   return -1;

  return dwDecBuffer.get(0);
 }

 public static int addBits(ByteBuffer lpDest, Integer iDestBitPos, ByteBuffer lpSource, Integer iBitSourcePos, Integer iBitLen) {
  // Get Buffer Lens
  int iSourceBufferBitLen = iBitLen + iBitSourcePos;
  int iTempBufferLen = getByteOfBit(iSourceBufferBitLen - 1);
  iTempBufferLen += 1 - getByteOfBit(iBitSourcePos);

  // Copy the Source Buffer
  ByteBuffer pTempBuffer = ByteBuffer.allocate(iTempBufferLen + 1);
  ByteBuffer pTempBufferCopyRange = lpSource.duplicate();
  pTempBufferCopyRange.position(getByteOfBit(iBitSourcePos));
  pTempBufferCopyRange.limit(iTempBufferLen);
  pTempBuffer.put(pTempBufferCopyRange);

  // Save the Last ibt if exist
  if ((iSourceBufferBitLen % 8) != 0) {
   byte val = pTempBuffer.get(iTempBufferLen - 1);
   val &= (byte) (255 << (8 - (iSourceBufferBitLen % 8)));
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
  byte startI = getByteOfBit(iDestBitPos);
  for (int i = startI; i < iNewTempBufferLen; i++) {
   byte val = lpDest.get(i);
   val |= pTempBuffer.get(i);
   lpDest.put(i, val);
  }

  // Return the number of bits of the new Dest Buffer
  return iDestBitPos + iBitLen;
 }

 public static byte getByteOfBit(int btByte) {
  byte val = (byte) (btByte >> 3);
  return val;
 }

 public static void shift(ByteBuffer lpBuff, int iSize, int ShiftLen) {
//  unsigned char * TempBuff = (unsigned char *)lpBuff;

  // Case no Shift Len
  if (ShiftLen != 0) {
   // Shift Right
   if (ShiftLen > 0) {
    if ((iSize - 1) > 0) {
     for (int i = (iSize - 1); i > 0; i--) {
      byte val = (byte) ((lpBuff.get(i - 1) << ((8 - ShiftLen))) | (lpBuff.get(i) >> ShiftLen));
      lpBuff.put(i, val);
     }
    }

    byte val = lpBuff.get(0);
    val >>= ShiftLen;
    lpBuff.put(0, val);
   } else    // Shift Left
   {
    ShiftLen = -ShiftLen;

    if ((iSize - 1) > 0) {
     for (int i = 0; i < (iSize - 1); i++) {
      byte val = (byte) ((lpBuff.get(i + 1) >> ((8 - ShiftLen))) | (lpBuff.get(i) << ShiftLen));
      lpBuff.put(i, val);
     }
    }

    byte val = lpBuff.get(iSize - 1);
    val <<= ShiftLen;
    lpBuff.put(iSize - 1, val);
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