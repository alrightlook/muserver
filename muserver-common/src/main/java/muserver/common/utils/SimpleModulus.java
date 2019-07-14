package muserver.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class SimpleModulus {
 public static final Integer[] XOR_KEYS = {
     0x3F08A79B, 0xE25CC287, 0x93D27AB9, 0x20DEA7BF
 };
 public static final Integer[] MODULUS_KEYS = {
     0x2D19919B, 0xE25C16D4, 0x9AED7BF8, 0x7E3C45D7
 };
 public static final Integer[] ENCRYPTION_KEYS = {
     0xEC9B8A9D, 0x3D7C615B, 0x9BED027C, 0x7C3CAD1B
 };
 public static final Integer[] DECRYPTION_KEYS = {
     0xED9BDF1B, 0x3C7C449D, 0x9BEDA8CF, 0x7C3C5DFE
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
    lpSource.position(lpSource.position() + 11);
    lpDest.position(lpDest.position() + 8);
   }
  }

  return iResult;
 }

 public static int decryptBlock(ByteBuffer lpDest, ByteBuffer lpSource) {
  lpDest.putLong(0);
//  memset(lpDest, 0, 8);
  ByteBuffer dwDecBuffer = ByteBuffer.allocate(Integer.BYTES * 4);
  int iBitPosition = 0;

  for (int i = 0; i < 4; i++) {
   addBits((ByteBuffer) dwDecBuffer.position(i), 0, lpSource, iBitPosition, 16);
   iBitPosition += 16;
   addBits((ByteBuffer) dwDecBuffer.position(i), 22, lpSource, iBitPosition, 2);
   iBitPosition += 2;
  }


  for (int i = 2; i >= 0; i--) {
   dwDecBuffer.put(i, (byte) (dwDecBuffer.get(i) ^ XOR_KEYS[i] ^ (dwDecBuffer.get(i + 1) & 0xFFFF)));
  }

  Integer Temp = 0, Temp1;


  for (int i = 0; i < 4; i++) {
   Temp1 = ((DECRYPTION_KEYS[i] * (dwDecBuffer.get(i))) %(MODULUS_KEYS[i])) ^ XOR_KEYS[i] ^ Temp;
   Temp = dwDecBuffer.get(i) & 0xFFFF;
   lpDest.putShort(i, Temp1.shortValue());
  }

  dwDecBuffer.put(0, (byte) 0);
  addBits((ByteBuffer) dwDecBuffer.position(0), 0, lpSource, iBitPosition, 16);
  dwDecBuffer.put(0, (byte) (dwDecBuffer.get(1) ^ dwDecBuffer.get(0) ^ 0x3D));


  byte btCheckSum = (byte) 0xF8;


  for (int i = 0; i < 8; i++)
   btCheckSum ^= lpDest.get(i);


  if (btCheckSum != (dwDecBuffer.get(1)))
   return -1;

  return dwDecBuffer.get(0);
 }

 public static int addBits(ByteBuffer lpDest, int iDestBitPos, ByteBuffer lpSource, int iBitSourcePos, int iBitLen) {
  // Get Buffer Lens
  int iSourceBufferBitLen = iBitLen + iBitSourcePos;
  int iTempBufferLen = getByteOfBit(iSourceBufferBitLen - 1);
  iTempBufferLen += 1 - getByteOfBit(iBitSourcePos);

  // Copy the Source Buffer
  ByteBuffer pTempBuffer = ByteBuffer.allocate(iTempBufferLen + 1);
  pTempBuffer.put((ByteBuffer) lpSource.position(getByteOfBit(iBitSourcePos)));

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
  ByteBuffer tempDist = ByteBuffer.allocate(iNewTempBufferLen);
  tempDist.put((ByteBuffer) lpDest.position(getByteOfBit(iDestBitPos)));


  for (int i = 0; i < iNewTempBufferLen; i++) {
   byte val = tempDist.get(i);
   val |= pTempBuffer.get(i);
   tempDist.put(i, val);
  }

  // Return the number of bits of the new Dest Buffer
  return iDestBitPos + iBitLen;
 }

 public static byte getByteOfBit(int btByte) {
  return (byte) (btByte >> 3);
 }

 public static void shift(ByteBuffer lpBuff, int iSize, int ShiftLen) {
//  unsigned char * TempBuff = (unsigned char *)lpBuff;


  // Case no Shift Len
  if (ShiftLen != 0) {
   // Shift Right
   if (ShiftLen > 0) {
    if ((iSize - 1) > 0) {
     for (int i = (iSize - 1); i > 0; i--) {
      lpBuff.put(i, (byte) ((lpBuff.get(i - 1) << ((8 - ShiftLen))) | (lpBuff.get(i) >> ShiftLen)));
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
      lpBuff.put(i, (byte) ((lpBuff.get(i + 1) >> ((8 - ShiftLen))) | (lpBuff.get(i) << ShiftLen)));
     }
    }

    byte val = lpBuff.get(iSize - 1);
    val <<= ShiftLen;
    lpBuff.put(iSize - 1, val);
   }
  }
 }

 public static boolean printAllKeys(File file, Short fileHeader) throws Exception {
  byte[] fileBuf = Files.readAllBytes(file.toPath());

  if (file.length() != 54) {
   throw new Exception("Invalid file length");
  }

  ByteArrayInputStream stream = new ByteArrayInputStream(fileBuf);

  if (fileHeader != EndianUtils.readShortLE(fileBuf, 0)) {
   throw new Exception("Invalid file header");
  }

  System.out.println("Modulus keys");
  Integer[] modulusKeys = new Integer[]{EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_KEYS[n] ^ modulusKeys[n]));
  }

  System.out.println("Encryption keys");
  Integer[] encryptionKeys = new Integer[]{EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_KEYS[n] ^ encryptionKeys[n]));
  }

  System.out.println("Decryption keys");
  Integer[] decryptionKeys = new Integer[]{EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream), EndianUtils.readIntegerBE(stream)};
  for (int n = 0; n < 4; n++) {
   System.out.println(String.format("%X", XOR_KEYS[n] ^ decryptionKeys[n]));
  }

  return true;
 }


 public static void main(String[] args) throws Exception {
  //0xC3 0x18 0x28 0x6F 0x32 0x33 0x90 0xA 0x70 0x35 0x51 0xFD 0xC8 0xFC 0x6D 0x13 0xA9 0x15 0x2F 0x92 0x0 0x0 0x31 0xF

  //Client
//  File dec2Dat = new File("/home/briankernighan/Desktop/Dec2.dat");
//  System.out.println(String.format("Print %s keys", dec2Dat.toString()));
//  if (!printAllKeys(dec2Dat, (short) 4370)) {
//   throw new Exception("Couldn't load Dec2.dat");
//  }
//
//  File enc1Dat = new File("/home/briankernighan/Desktop/Enc1.dat");
//  System.out.println(String.format("Print %s keys", enc1Dat.toString()));
//  if (!printAllKeys(enc1Dat, (short) 4370)) {
//   throw new Exception("Couldn't load Enc1.dat");
//  }

  //Server
  File dec1Dat = new File("/home/briankernighan/Desktop/Dec1.dat");
  System.out.println(String.format("Print %s keys", dec1Dat.toString()));
  if (!printAllKeys(dec1Dat, (short) 4370)) {
   throw new Exception("Couldn't load Dec2.dat");
  }

  File enc2Dat = new File("/home/briankernighan/Desktop/Enc2.dat");
  System.out.println(String.format("Print %s keys", enc2Dat.toString()));
  if (!printAllKeys(enc2Dat, (short) 4370)) {
   throw new Exception("Couldn't load Enc1.dat");
  }
 }
}