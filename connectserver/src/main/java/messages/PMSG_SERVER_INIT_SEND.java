package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PMSG_SERVER_INIT_SEND {
    @Nullable
    public abstract PBMSG_HEAD header();

    @Nullable
    public abstract Byte result();

    public static PMSG_SERVER_INIT_SEND create(PBMSG_HEAD header, Byte result) {
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

        public abstract Builder result(Byte result);

        public abstract PMSG_SERVER_INIT_SEND build();
    }
}
