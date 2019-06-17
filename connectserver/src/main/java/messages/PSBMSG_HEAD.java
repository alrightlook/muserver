package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PSBMSG_HEAD {
    public abstract byte type();
    public abstract byte size();
    public abstract byte head();
    public abstract byte subH();

    public static PSBMSG_HEAD create(byte type, byte size, byte head, byte subH) {
        return builder()
                .type(type)
                .size(size)
                .head(head)
                .subH(subH)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PSBMSG_HEAD.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder type(byte type);

        public abstract Builder size(byte size);

        public abstract Builder head(byte head);

        public abstract Builder subH(byte subH);

        public abstract PSBMSG_HEAD build();
    }
}
