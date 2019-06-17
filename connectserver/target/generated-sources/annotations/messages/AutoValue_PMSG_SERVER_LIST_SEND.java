

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PMSG_SERVER_LIST_SEND extends PMSG_SERVER_LIST_SEND {

  private final PSWMSG_HEAD header;

  private final short count;

  private AutoValue_PMSG_SERVER_LIST_SEND(
      PSWMSG_HEAD header,
      short count) {
    this.header = header;
    this.count = count;
  }

  @Override
  public PSWMSG_HEAD header() {
    return header;
  }

  @Override
  public short count() {
    return count;
  }

  @Override
  public String toString() {
    return "PMSG_SERVER_LIST_SEND{"
         + "header=" + header + ", "
         + "count=" + count
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PMSG_SERVER_LIST_SEND) {
      PMSG_SERVER_LIST_SEND that = (PMSG_SERVER_LIST_SEND) o;
      return (this.header.equals(that.header()))
           && (this.count == that.count());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= header.hashCode();
    h$ *= 1000003;
    h$ ^= count;
    return h$;
  }

  static final class Builder extends PMSG_SERVER_LIST_SEND.Builder {
    private PSWMSG_HEAD header;
    private Short count;
    Builder() {
    }
    @Override
    public PMSG_SERVER_LIST_SEND.Builder header(PSWMSG_HEAD header) {
      if (header == null) {
        throw new NullPointerException("Null header");
      }
      this.header = header;
      return this;
    }
    @Override
    public PMSG_SERVER_LIST_SEND.Builder count(short count) {
      this.count = count;
      return this;
    }
    @Override
    public PMSG_SERVER_LIST_SEND build() {
      String missing = "";
      if (this.header == null) {
        missing += " header";
      }
      if (this.count == null) {
        missing += " count";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PMSG_SERVER_LIST_SEND(
          this.header,
          this.count);
    }
  }

}
