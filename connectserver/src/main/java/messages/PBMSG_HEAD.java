package messages;

import com.google.auto.value.AutoValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.annotation.Nullable;

@AutoValue
public abstract class PBMSG_HEAD {
    @Nullable
    public abstract Byte type();

    @Nullable
    public abstract Byte size();

    @Nullable
    public abstract Byte head();

    public static PBMSG_HEAD create(Byte type, Byte size, Byte head) {
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
        public abstract Builder type(Byte type);

        public abstract Builder size(Byte size);

        public abstract Builder head(Byte head);

        public abstract PBMSG_HEAD build();
    }
}