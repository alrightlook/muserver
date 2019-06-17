package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PMSG_SERVER_LIST_SEND {
    public abstract PSWMSG_HEAD header();
    public abstract short count();

    public static PMSG_SERVER_LIST_SEND create(PSWMSG_HEAD header, short count) {
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

        public abstract Builder count(short count);

        public abstract PMSG_SERVER_LIST_SEND build();
    }
}
