

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PSWMSG_HEAD extends PSWMSG_HEAD {

  private final byte type;

  private final short size;

  private final byte head;

  private final byte subH;

  private AutoValue_PSWMSG_HEAD(
      byte type,
      short size,
      byte head,
      byte subH) {
    this.type = type;
    this.size = size;
    this.head = head;
    this.subH = subH;
  }

  @Override
  public byte type() {
    return type;
  }

  @Override
  public short size() {
    return size;
  }

  @Override
  public byte head() {
    return head;
  }

  @Override
  public byte subH() {
    return subH;
  }

  @Override
  public String toString() {
    return "PSWMSG_HEAD{"
         + "type=" + type + ", "
         + "size=" + size + ", "
         + "head=" + head + ", "
         + "subH=" + subH
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PSWMSG_HEAD) {
      PSWMSG_HEAD that = (PSWMSG_HEAD) o;
      return (this.type == that.type())
           && (this.size == that.size())
           && (this.head == that.head())
           && (this.subH == that.subH());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= type;
    h$ *= 1000003;
    h$ ^= size;
    h$ *= 1000003;
    h$ ^= head;
    h$ *= 1000003;
    h$ ^= subH;
    return h$;
  }

  static final class Builder extends PSWMSG_HEAD.Builder {
    private Byte type;
    private Short size;
    private Byte head;
    private Byte subH;
    Builder() {
    }
    @Override
    public PSWMSG_HEAD.Builder type(byte type) {
      this.type = type;
      return this;
    }
    @Override
    public PSWMSG_HEAD.Builder size(short size) {
      this.size = size;
      return this;
    }
    @Override
    public PSWMSG_HEAD.Builder head(byte head) {
      this.head = head;
      return this;
    }
    @Override
    public PSWMSG_HEAD.Builder subH(byte subH) {
      this.subH = subH;
      return this;
    }
    @Override
    public PSWMSG_HEAD build() {
      String missing = "";
      if (this.type == null) {
        missing += " type";
      }
      if (this.size == null) {
        missing += " size";
      }
      if (this.head == null) {
        missing += " head";
      }
      if (this.subH == null) {
        missing += " subH";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PSWMSG_HEAD(
          this.type,
          this.size,
          this.head,
          this.subH);
    }
  }

}
