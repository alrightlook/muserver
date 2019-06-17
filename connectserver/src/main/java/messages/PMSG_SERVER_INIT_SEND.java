package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PMSG_SERVER_INIT_SEND {
    public abstract PBMSG_HEAD header();
    public abstract byte result();

    public static PMSG_SERVER_INIT_SEND create(PBMSG_HEAD header, byte result) {
        return builder()
                .header(header)
                .result(result)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PMSG_SERVER_INIT_SEND.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder result(byte result);

        public abstract PMSG_SERVER_INIT_SEND build();
    }
}
