package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PWMSG_HEAD {
    public abstract byte type();
    public abstract short size();
    public abstract byte head();

    public static PWMSG_HEAD create(byte type, short size, byte head) {
        return builder()
                .type(type)
                .size(size)
                .head(head)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PWMSG_HEAD.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder type(byte type);

        public abstract Builder size(short size);

        public abstract Builder head(byte head);

        public abstract PWMSG_HEAD build();
    }
}
