

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PMSG_SERVER_INIT_SEND extends PMSG_SERVER_INIT_SEND {

  private final PBMSG_HEAD header;

  private final byte result;

  private AutoValue_PMSG_SERVER_INIT_SEND(
      PBMSG_HEAD header,
      byte result) {
    this.header = header;
    this.result = result;
  }

  @Override
  public PBMSG_HEAD header() {
    return header;
  }

  @Override
  public byte result() {
    return result;
  }

  @Override
  public String toString() {
    return "PMSG_SERVER_INIT_SEND{"
         + "header=" + header + ", "
         + "result=" + result
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PMSG_SERVER_INIT_SEND) {
      PMSG_SERVER_INIT_SEND that = (PMSG_SERVER_INIT_SEND) o;
      return (this.header.equals(that.header()))
           && (this.result == that.result());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= header.hashCode();
    h$ *= 1000003;
    h$ ^= result;
    return h$;
  }

  static final class Builder extends PMSG_SERVER_INIT_SEND.Builder {
    private PBMSG_HEAD header;
    private Byte result;
    Builder() {
    }
    @Override
    public PMSG_SERVER_INIT_SEND.Builder header(PBMSG_HEAD header) {
      if (header == null) {
        throw new NullPointerException("Null header");
      }
      this.header = header;
      return this;
    }
    @Override
    public PMSG_SERVER_INIT_SEND.Builder result(byte result) {
      this.result = result;
      return this;
    }
    @Override
    public PMSG_SERVER_INIT_SEND build() {
      String missing = "";
      if (this.header == null) {
        missing += " header";
      }
      if (this.result == null) {
        missing += " result";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PMSG_SERVER_INIT_SEND(
          this.header,
          this.result);
    }
  }

}
