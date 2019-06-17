package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PMSG_SERVER_INFO_RECV {
    @Nullable
    public abstract PSBMSG_HEAD header();

    @Nullable
    public abstract Byte serverCode();

    public static PMSG_SERVER_INFO_RECV create(PSBMSG_HEAD header, Byte serverCode) {
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

        public abstract Builder serverCode(Byte serverCode);

        public abstract PMSG_SERVER_INFO_RECV build();
    }
}
