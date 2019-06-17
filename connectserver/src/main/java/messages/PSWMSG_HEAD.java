package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PSWMSG_HEAD {
    public abstract byte type();
    public abstract short size();
    public abstract byte head();
    public abstract byte subH();

    public static PSWMSG_HEAD create(byte type, short size, byte head, byte subH) {
        return builder()
                .type(type)
                .size(size)
                .head(head)
                .subH(subH)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PSWMSG_HEAD.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder type(byte type);

        public abstract Builder size(short size);

        public abstract Builder head(byte head);

        public abstract Builder subH(byte subH);

        public abstract PSWMSG_HEAD build();
    }
}
