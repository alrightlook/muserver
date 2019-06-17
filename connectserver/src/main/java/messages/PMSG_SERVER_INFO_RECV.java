package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PMSG_SERVER_INFO_RECV {
   public abstract PSBMSG_HEAD header();
   public abstract byte serverCode();

    public static PMSG_SERVER_INFO_RECV create(PSBMSG_HEAD header, byte serverCode) {
        return builder()
                .header(header)
                .serverCode(serverCode)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PMSG_SERVER_INFO_RECV.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PSBMSG_HEAD header);

        public abstract Builder serverCode(byte serverCode);

        public abstract PMSG_SERVER_INFO_RECV build();
    }
}
