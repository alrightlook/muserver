

package messages;

import java.util.Arrays;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PMSG_SERVER_INFO_SEND extends PMSG_SERVER_INFO_SEND {

  private final PSBMSG_HEAD header;

  private final byte[] serverAddress;

  private final short serverPort;

  private AutoValue_PMSG_SERVER_INFO_SEND(
      PSBMSG_HEAD header,
      byte[] serverAddress,
      short serverPort) {
    this.header = header;
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
  }

  @Override
  public PSBMSG_HEAD header() {
    return header;
  }

  @Override
  public byte[] serverAddress() {
    return serverAddress;
  }

  @Override
  public short serverPort() {
    return serverPort;
  }

  @Override
  public String toString() {
    return "PMSG_SERVER_INFO_SEND{"
         + "header=" + header + ", "
         + "serverAddress=" + Arrays.toString(serverAddress) + ", "
         + "serverPort=" + serverPort
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PMSG_SERVER_INFO_SEND) {
      PMSG_SERVER_INFO_SEND that = (PMSG_SERVER_INFO_SEND) o;
      return (this.header.equals(that.header()))
           && (Arrays.equals(this.serverAddress, (that instanceof AutoValue_PMSG_SERVER_INFO_SEND) ? ((AutoValue_PMSG_SERVER_INFO_SEND) that).serverAddress : that.serverAddress()))
           && (this.serverPort == that.serverPort());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= header.hashCode();
    h$ *= 1000003;
    h$ ^= Arrays.hashCode(serverAddress);
    h$ *= 1000003;
    h$ ^= serverPort;
    return h$;
  }

  static final class Builder extends PMSG_SERVER_INFO_SEND.Builder {
    private PSBMSG_HEAD header;
    private byte[] serverAddress;
    private Short serverPort;
    Builder() {
    }
    @Override
    public PMSG_SERVER_INFO_SEND.Builder header(PSBMSG_HEAD header) {
      if (header == null) {
        throw new NullPointerException("Null header");
      }
      this.header = header;
      return this;
    }
    @Override
    public PMSG_SERVER_INFO_SEND.Builder serverAddress(byte[] serverAddress) {
      if (serverAddress == null) {
        throw new NullPointerException("Null serverAddress");
      }
      this.serverAddress = serverAddress;
      return this;
    }
    @Override
    public PMSG_SERVER_INFO_SEND.Builder serverPort(short serverPort) {
      this.serverPort = serverPort;
      return this;
    }
    @Override
    public PMSG_SERVER_INFO_SEND build() {
      String missing = "";
      if (this.header == null) {
        missing += " header";
      }
      if (this.serverAddress == null) {
        missing += " serverAddress";
      }
      if (this.serverPort == null) {
        missing += " serverPort";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PMSG_SERVER_INFO_SEND(
          this.header,
          this.serverAddress,
          this.serverPort);
    }
  }

}
