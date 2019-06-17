

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PMSG_SERVER_LIST extends PMSG_SERVER_LIST {

  private final short serverCode;

  private final byte userTotal;

  private final byte type;

  private AutoValue_PMSG_SERVER_LIST(
      short serverCode,
      byte userTotal,
      byte type) {
    this.serverCode = serverCode;
    this.userTotal = userTotal;
    this.type = type;
  }

  @Override
  public short serverCode() {
    return serverCode;
  }

  @Override
  public byte userTotal() {
    return userTotal;
  }

  @Override
  public byte type() {
    return type;
  }

  @Override
  public String toString() {
    return "PMSG_SERVER_LIST{"
         + "serverCode=" + serverCode + ", "
         + "userTotal=" + userTotal + ", "
         + "type=" + type
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PMSG_SERVER_LIST) {
      PMSG_SERVER_LIST that = (PMSG_SERVER_LIST) o;
      return (this.serverCode == that.serverCode())
           && (this.userTotal == that.userTotal())
           && (this.type == that.type());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= serverCode;
    h$ *= 1000003;
    h$ ^= userTotal;
    h$ *= 1000003;
    h$ ^= type;
    return h$;
  }

  static final class Builder extends PMSG_SERVER_LIST.Builder {
    private Short serverCode;
    private Byte userTotal;
    private Byte type;
    Builder() {
    }
    @Override
    public PMSG_SERVER_LIST.Builder serverCode(short serverCode) {
      this.serverCode = serverCode;
      return this;
    }
    @Override
    public PMSG_SERVER_LIST.Builder userTotal(byte userTotal) {
      this.userTotal = userTotal;
      return this;
    }
    @Override
    public PMSG_SERVER_LIST.Builder type(byte type) {
      this.type = type;
      return this;
    }
    @Override
    public PMSG_SERVER_LIST build() {
      String missing = "";
      if (this.serverCode == null) {
        missing += " serverCode";
      }
      if (this.userTotal == null) {
        missing += " userTotal";
      }
      if (this.type == null) {
        missing += " type";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_PMSG_SERVER_LIST(
          this.serverCode,
          this.userTotal,
          this.type);
    }
  }

}
