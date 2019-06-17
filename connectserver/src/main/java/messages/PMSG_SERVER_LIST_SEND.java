package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PMSG_SERVER_LIST_SEND {
    @Nullable
    public abstract PSWMSG_HEAD header();

    @Nullable
    public abstract Short count();

    public static PMSG_SERVER_LIST_SEND create(PSWMSG_HEAD header, Short count) {
        return builder()
                .header(header)
                .count(count)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PMSG_SERVER_LIST_SEND.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PSWMSG_HEAD header);

        public abstract Builder count(Short count);

        public abstract PMSG_SERVER_LIST_SEND build();
    }
}