package decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.util.List;

public class UdpPacketHeaderDecoder extends MessageToMessageDecoder<DatagramPacket> {
 private final static Logger logger = LogManager.getLogger(UdpPacketHeaderDecoder.class);

 @Override
 protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> list) throws Exception {
  logger.info(String.format("Addr: %s", packet.getAddress().toString()));
  logger.info(String.format("Port: %d", packet.getPort()));
  logger.info(String.format("Offset: %d", packet.getOffset()));
  logger.info(String.format("Length: %d", packet.getLength()));
  logger.info(String.format("Data: %s", packet.getData()));
 }
}
