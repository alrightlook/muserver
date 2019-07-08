package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CommonConfigs extends AbstractConfigs {
    public static Builder builder() {
        return new AutoValue_CommonConfigs.Builder();
    }

    @JsonProperty("joinServer")
    public abstract JoinServerConfigs joinServer();

    @JsonProperty("connectServer")
    public abstract ConnectServerConfigs connectServer();

    @JsonCreator
    public static CommonConfigs create(
            @JsonProperty("joinServer") JoinServerConfigs joinServer,
            @JsonProperty("connectServer") ConnectServerConfigs connectServer
    ) {
        return builder()
                .joinServer(joinServer)
                .connectServer(connectServer)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder joinServer(JoinServerConfigs joinServer);

        public abstract Builder connectServer(ConnectServerConfigs connectServer);

        public abstract CommonConfigs build();
    }
}