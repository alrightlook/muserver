package muserver.joinserver.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import muserver.common.utils.EndianUtils;
import muserver.common.utils.HexUtils;
import muserver.common.utils.SimpleModulus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class SimpleModulusTest {
 @Test
 public void test() {
  ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

  System.out.println(String.format("byteBuf readable bytes: %d", byteBuf.readableBytes()));

  ByteBuf slice_1 = byteBuf.copy(0, 2);

  System.out.println(String.format("slice_1 readable bytes: %d", slice_1.readableBytes()));

  for (int i = 0; i < slice_1.readableBytes(); i++) {
   slice_1.setByte(i, 100);
  }

  for (int i = 0; i < byteBuf.readableBytes(); i++) {
   System.out.println(byteBuf.getByte(i));
  }
 }

 @Test
 public void testC1PacketEncryption() throws Exception {
  byte[] c1Packet = new byte[] {(byte) 0xC1, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x0F, 0x0F, 0x0F, 0x0F, 0x0F };
  ByteBuffer lpSrc = ByteBuffer.wrap(c1Packet);
  ByteBuffer lpDest = ByteBuffer.allocate(c1Packet.length);
//  SimpleModulus.encrypt(lpDest, lpSrc, c1Packet.length);
  HexUtils.toString(lpDest.array());
 }

 @Test
 public void testC3PacketDecryption() throws Exception {
  byte[] c3Packet = new byte[]{(byte) 0xC3, 0x18, 0x28, 0x6F, 0x32, 0x33, (byte) 0x90, 0xA, 0x70, 0x35, 0x51, (byte) 0xFD, (byte) 0xC8, (byte) 0xFC, 0x6D, 0x13, (byte) 0xA9, 0x15, 0x2F, (byte) 0x92, 0x0, 0x0, 0x31, 0xF};
  ByteBuf decBuff = Unpooled.buffer(c3Packet.length), lpMsg = Unpooled.wrappedBuffer(Arrays.copyOfRange(c3Packet, 2, c3Packet.length - 2));
  Integer decSize = SimpleModulus.decrypt(decBuff, lpMsg, c3Packet.length - 2) + 1;
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
