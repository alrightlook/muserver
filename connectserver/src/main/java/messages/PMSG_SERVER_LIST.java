package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PMSG_SERVER_LIST {
    public abstract short serverCode();
    public abstract byte userTotal();
    public abstract byte type();

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
        public abstract Builder serverCode(short serverCode);

        public abstract Builder userTotal(byte userTotal);

        public abstract Builder type(byte type);

        public abstract PMSG_SERVER_LIST build();
    }
}
