import io.netty.channel.ChannelHandlerContext;

public class ConnectClient {
    private final ChannelHandlerContext ctx;
    private final Integer index;

    public ConnectClient(ChannelHandlerContext ctx, Integer index) {
        this.ctx = ctx;
        this.index = index;
    }

    public ChannelHandlerContext ctx() {
        return ctx;
    }

    public Integer index() {
        return index;
    }

    public static ConnectClient create(ChannelHandlerContext ctx, Integer index) {
        return new ConnectClient(ctx, index);
    }
}
