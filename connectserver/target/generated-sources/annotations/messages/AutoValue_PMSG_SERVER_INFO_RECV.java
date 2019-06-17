

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PMSG_SERVER_INFO_RECV extends PMSG_SERVER_INFO_RECV {

  private final PSBMSG_HEAD header;

  private final byte serverCode;

  private AutoValue_PMSG_SERVER_INFO_RECV(
      PSBMSG_HEAD header,
      byte serverCode) {
    this.header = header;
    this.serverCode = serverCode;
  }

  @Override
  public PSBMSG_HEAD header() {
    return header;
  }

  @Override
  public byte serverCode() {
    return serverCode;
  }

  @Override
  public String toString() {
    return "PMSG_SERVER_INFO_RECV{"
         + "header=" + header + ", "
         + "serverCode=" + serverCode
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PMSG_SERVER_INFO_RECV) {
      PMSG_SERVER_INFO_RECV that = (PMSG_SERVER_INFO_RECV) o;
      return (this.header.equals(that.header()))
           && (this.serverCode == that.serverCode());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= header.hashCode();
    h$ *= 1000003;
    h$ ^= serverCode;
    return h$;
  }

  static final class Builder extends PMSG_SERVER_INFO_RECV.Builder {
    private PSBMSG_HEAD header;
    private Byte serverCode;
    Builder() {
    }
    @Override
    public PMSG_SERVER_INFO_RECV.Builder header(PSBMSG_HEAD header) {
      if (header == null) {
        throw new NullPointerException("Null header");
      }
      this.header = header;
      return this;
    }
    @Override
    public PMSG_SERVER_INFO_RECV.Builder serverCode(byte serverCode) {
      this.serverCode = serverCode;
      return this;
    }
    @Override
    public PMSG_SERVER_INFO_RECV build() {
      String missing = "";
      if (this.header == null) {
        missing += " header";
      }
      if (this.serverCode == null) {
        missing += " serverCode";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PMSG_SERVER_INFO_RECV(
          this.header,
          this.serverCode);
    }
  }

}
