

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PBMSG_HEAD extends PBMSG_HEAD {

  private final byte type;

  private final byte size;

  private final byte head;

  private AutoValue_PBMSG_HEAD(
      byte type,
      byte size,
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
  public byte size() {
    return size;
  }

  @Override
  public byte head() {
    return head;
  }

  @Override
  public String toString() {
    return "PBMSG_HEAD{"
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
    if (o instanceof PBMSG_HEAD) {
      PBMSG_HEAD that = (PBMSG_HEAD) o;
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

  static final class Builder extends PBMSG_HEAD.Builder {
    private Byte type;
    private Byte size;
    private Byte head;
    Builder() {
    }
    @Override
    public PBMSG_HEAD.Builder type(byte type) {
      this.type = type;
      return this;
    }
    @Override
    public PBMSG_HEAD.Builder size(byte size) {
      this.size = size;
      return this;
    }
    @Override
    public PBMSG_HEAD.Builder head(byte head) {
      this.head = head;
      return this;
    }
    @Override
    public PBMSG_HEAD build() {
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
      return new AutoValue_PBMSG_HEAD(
          this.type,
          this.size,
          this.head);
    }
  }

}
