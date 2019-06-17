package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PWMSG_HEAD {
    @Nullable
    public abstract Byte type();

    @Nullable
    public abstract Short size();

    @Nullable
    public abstract Byte head();

    public static PWMSG_HEAD create(Byte type, Short size, Byte head) {
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
        public abstract Builder type(Byte type);

        public abstract Builder size(Short size);

        public abstract Builder head(Byte head);

        public abstract PWMSG_HEAD build();
    }
}