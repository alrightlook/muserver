package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PBMSG_HEAD {
    public abstract byte type();
    public abstract byte size();
    public abstract byte head();

    public static PBMSG_HEAD create(byte type, byte size, byte head) {
        return builder()
                .type(type)
                .size(size)
                .head(head)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PBMSG_HEAD.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder type(byte type);

        public abstract Builder size(byte size);

        public abstract Builder head(byte head);

        public abstract PBMSG_HEAD build();
    }
}