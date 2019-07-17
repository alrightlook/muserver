package muserver.common.utils;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
 public static ByteBuffer deepCopy(ByteBuffer source, int offset) {
  source.position(offset).limit(source.capacity());
  ByteBuffer byteBuffer;
  if (!source.isDirect()) {
   byteBuffer = ByteBuffer.allocate(source.remaining());
   byteBuffer.put(source);
   byteBuffer.flip();
   byteBuffer.order(source.order());
   return byteBuffer;
  } else {
   byteBuffer = ByteBuffer.allocateDirect(source.remaining());
   byteBuffer.put(source);
   byteBuffer.flip();
   byteBuffer.order(source.order());
   return byteBuffer;
  }
 }
}