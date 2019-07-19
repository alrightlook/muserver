package muserver.joinserver.common.utils;

import muserver.common.utils.HexUtils;
import muserver.common.utils.SimpleModulus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.ByteBuffer;

public class SimpleModulusTest {
 @Test
 public void testC1PacketEncryption() throws Exception {
  byte[] c1Packet = new byte[] {(byte) 0xC1, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x0F, 0x0F, 0x0F, 0x0F, 0x0F };
  ByteBuffer lpSrc = ByteBuffer.wrap(c1Packet);
  ByteBuffer lpDest = ByteBuffer.allocate(c1Packet.length);
  SimpleModulus.encrypt(lpDest, lpSrc, c1Packet.length);
  HexUtils.toString(lpDest.array());
 }

 @Test
 public void testC3PacketDecryption() throws Exception {
  byte[] c3Packet = new byte[]{(byte) 0xC3, 0x18, 0x28, 0x6F, 0x32, 0x33, (byte) 0x90, 0xA, 0x70, 0x35, 0x51, (byte) 0xFD, (byte) 0xC8, (byte) 0xFC, 0x6D, 0x13, (byte) 0xA9, 0x15, 0x2F, (byte) 0x92, 0x0, 0x0, 0x31, 0xF};
  ByteBuffer lpMsg = ByteBuffer.wrap(c3Packet);
  ByteBuffer decBuff = ByteBuffer.allocate(c3Packet.length);
  Integer decSize = SimpleModulus.decrypt((ByteBuffer) decBuff.position(decBuff.position() + 1), (ByteBuffer) lpMsg.position(lpMsg.position() + 2), c3Packet.length - 2) + 1;
  byte header = (byte) 0xC1;
  decBuff.put(0, header);
  decBuff.put(1, decSize.byteValue());
  System.out.println(HexUtils.toString(decBuff.array()));
 }

 @Test
 public void readKeys() throws Exception {
//  Client
  File dec2Dat = new File("/home/briankernighan/Desktop/Dec2.dat");
  System.out.println(String.format("Print %s keys", dec2Dat.toString()));
  Assertions.assertTrue(SimpleModulus.printAllKeys(dec2Dat));

  File enc1Dat = new File("/home/briankernighan/Desktop/Enc1.dat");
  System.out.println(String.format("Print %s keys", enc1Dat.toString()));
  Assertions.assertTrue(SimpleModulus.printAllKeys(enc1Dat));

//  Server
  File dec1Dat = new File("/home/victorchicu/Desktop/Dec1.dat");
  System.out.println(String.format("Print %s keys", dec1Dat.toString()));
  Assertions.assertTrue(SimpleModulus.printAllKeys(dec1Dat));

  File enc2Dat = new File("/home/victorchicu/Desktop/Enc2.dat");
  System.out.println(String.format("Print %s keys", enc2Dat.toString()));
  Assertions.assertTrue(SimpleModulus.printAllKeys(enc2Dat));
 }
}
