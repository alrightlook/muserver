package decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UdpPacketHeaderDecoder extends MessageToMessageDecoder<DatagramPacket> {
 private final static Logger logger = LogManager.getLogger(UdpPacketHeaderDecoder.class);

 @Override
 protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> list) throws Exception {
  logger.info("UDP PACKET HEADER DECODER");
  logger.info(packet.toString());
 }
}
