

package messages;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_PMSG_SERVER_LIST_RECV extends PMSG_SERVER_LIST_RECV {

  private final PSBMSG_HEAD header;

  AutoValue_PMSG_SERVER_LIST_RECV(
      PSBMSG_HEAD header) {
    if (header == null) {
      throw new NullPointerException("Null header");
    }
    this.header = header;
  }

  @Override
  public PSBMSG_HEAD header() {
    return header;
  }

  @Override
  public String toString() {
    return "PMSG_SERVER_LIST_RECV{"
         + "header=" + header
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof PMSG_SERVER_LIST_RECV) {
      PMSG_SERVER_LIST_RECV that = (PMSG_SERVER_LIST_RECV) o;
      return (this.header.equals(that.header()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= header.hashCode();
    return h$;
  }

}
