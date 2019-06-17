package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PSWMSG_HEAD {
    @Nullable
    public abstract Byte type();

    @Nullable
    public abstract Short size();

    @Nullable
    public abstract Byte head();

    @Nullable
    public abstract Byte subH();


    public static PSWMSG_HEAD create(Byte type, Short size, Byte head, Byte subH) {
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
        public abstract Builder type(Byte type);

        public abstract Builder size(Short size);

        public abstract Builder head(Byte head);

        public abstract Builder subH(Byte subH);

        public abstract PSWMSG_HEAD build();
    }
}
