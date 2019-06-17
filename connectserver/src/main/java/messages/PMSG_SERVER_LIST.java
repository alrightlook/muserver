package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PMSG_SERVER_LIST {
    @Nullable
    public abstract Short serverCode();

    @Nullable
    public abstract Byte userTotal();

    @Nullable
    public abstract Byte type();

    public static PMSG_SERVER_LIST create(short serverCode, byte userTotal, byte type) {
        return builder()
                .serverCode(serverCode)
                .userTotal(userTotal)
                .type(type)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PMSG_SERVER_LIST.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder serverCode(Short serverCode);

        public abstract Builder userTotal(Byte userTotal);

        public abstract Builder type(Byte type);

        public abstract PMSG_SERVER_LIST build();
    }
}
