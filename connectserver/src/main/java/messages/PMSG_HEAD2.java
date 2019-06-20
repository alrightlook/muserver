package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_HEAD2 extends AbstractPacket {
    public static Builder builder() {
        return new AutoValue_PMSG_HEAD2.Builder();
    }

    public static PMSG_HEAD2 create(Byte type, Byte size, Byte headCode, Byte subCode) {
        return builder()
                .type(type)
                .size(size)
                .headCode(headCode)
                .subCode(subCode)
                .build();
    }

    public abstract Byte type();

    public abstract Byte size();

    public abstract Byte headCode();

    public abstract Byte subCode();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        StreamUtils.writeByte(type(), stream);
        StreamUtils.writeByte(size(), stream);
        StreamUtils.writeByte(headCode(), stream);
        StreamUtils.writeByte(subCode(), stream);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder type(Byte type);

        public abstract Builder size(Byte size);

        public abstract Builder headCode(Byte headCode);

        public abstract Builder subCode(Byte subCode);

        public abstract PMSG_HEAD2 build();
    }
}
