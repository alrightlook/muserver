package messages;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PMSG_SERVER_INFO_SEND {
    public abstract PSBMSG_HEAD header();
    public abstract byte[] serverAddress();
    public abstract short serverPort();

    public static PMSG_SERVER_INFO_SEND create(PSBMSG_HEAD header, byte[] serverAddress, short serverPort) {
        return builder()
                .header(header)
                .serverAddress(serverAddress)
                .serverPort(serverPort)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PMSG_SERVER_INFO_SEND.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PSBMSG_HEAD header);

        public abstract Builder serverAddress(byte[] serverAddress);

        public abstract Builder serverPort(short serverPort);

        public abstract PMSG_SERVER_INFO_SEND build();
    }
}
