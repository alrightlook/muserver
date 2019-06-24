package tasks;

import messages.AbstractPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class HealthTask extends TimerTask {
 private final static int PACKET_TIMEOUT = 1000 * 5;
 private final static Logger logger = LogManager.getLogger(HealthTask.class);

 private final ConcurrentHashMap<Byte, AbstractPacket> packets;

 public HealthTask(ConcurrentHashMap<Byte, AbstractPacket> packets) {
  this.packets = packets;
 }

 @Override
 public void run() {
  for (Byte headCode : packets.keySet()) {
   switch (headCode) {
    case 1: {
     AbstractPacket abstractPacket = packets.get(headCode);
     if (new Date().getTime() - abstractPacket.packetTime().getTime() > PACKET_TIMEOUT) {
      logger.warn("Connection to Game server has been interrupted");
     }
    }
    break;
    case 2: {
     AbstractPacket abstractPacket = packets.get(headCode);
     if (new Date().getTime() - abstractPacket.packetTime().getTime() > PACKET_TIMEOUT) {
      logger.warn("Connection to Join server has been interrupted");
     }
    }
    break;
   }
  }
 }
}
