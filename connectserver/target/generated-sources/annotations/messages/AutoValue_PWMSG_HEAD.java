

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PWMSG_HEAD extends PWMSG_HEAD {

  private final byte type;

  private final short size;

  private final byte head;

  private AutoValue_PWMSG_HEAD(
      byte type,
      short size,
      byte head) {
    this.type = type;
    this.size = size;
    this.head = head;
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
  public String toString() {
    return "PWMSG_HEAD{"
         + "type=" + type + ", "
         + "size=" + size + ", "
         + "head=" + head
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PWMSG_HEAD) {
      PWMSG_HEAD that = (PWMSG_HEAD) o;
      return (this.type == that.type())
           && (this.size == that.size())
           && (this.head == that.head());
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
    return h$;
  }

  static final class Builder extends PWMSG_HEAD.Builder {
    private Byte type;
    private Short size;
    private Byte head;
    Builder() {
    }
    @Override
    public PWMSG_HEAD.Builder type(byte type) {
      this.type = type;
      return this;
    }
    @Override
    public PWMSG_HEAD.Builder size(short size) {
      this.size = size;
      return this;
    }
    @Override
    public PWMSG_HEAD.Builder head(byte head) {
      this.head = head;
      return this;
    }
    @Override
    public PWMSG_HEAD build() {
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
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PWMSG_HEAD(
          this.type,
          this.size,
          this.head);
    }
  }

}
