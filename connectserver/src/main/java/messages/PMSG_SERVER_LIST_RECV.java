package messages;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PMSG_SERVER_LIST_RECV {
    @Nullable
    public abstract PSBMSG_HEAD header();
}