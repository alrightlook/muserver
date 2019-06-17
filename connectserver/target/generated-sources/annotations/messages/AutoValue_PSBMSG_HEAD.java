

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PSBMSG_HEAD extends PSBMSG_HEAD {

  private final byte type;

  private final byte size;

  private final byte head;

  private final byte subH;

  private AutoValue_PSBMSG_HEAD(
      byte type,
      byte size,
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
  public byte size() {
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
    return "PSBMSG_HEAD{"
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
    if (o instanceof PSBMSG_HEAD) {
      PSBMSG_HEAD that = (PSBMSG_HEAD) o;
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

  static final class Builder extends PSBMSG_HEAD.Builder {
    private Byte type;
    private Byte size;
    private Byte head;
    private Byte subH;
    Builder() {
    }
    @Override
    public PSBMSG_HEAD.Builder type(byte type) {
      this.type = type;
      return this;
    }
    @Override
    public PSBMSG_HEAD.Builder size(byte size) {
      this.size = size;
      return this;
    }
    @Override
    public PSBMSG_HEAD.Builder head(byte head) {
      this.head = head;
      return this;
    }
    @Override
    public PSBMSG_HEAD.Builder subH(byte subH) {
      this.subH = subH;
      return this;
    }
    @Override
    public PSBMSG_HEAD build() {
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
      return new AutoValue_PSBMSG_HEAD(
          this.type,
          this.size,
          this.head,
          this.subH);
    }
  }

}
