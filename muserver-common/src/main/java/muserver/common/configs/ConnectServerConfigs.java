package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ConnectServerConfigs extends AbstractConfigs {
    public static Builder builder() {
        return new AutoValue_ConnectServerConfigs.Builder();
    }

    @JsonProperty("hostname")
    public abstract String hostname();

    @JsonProperty("tcpPort")
    public abstract Integer tcpPort();

    @JsonProperty("udpPort")
    public abstract Integer udpPort();

    @JsonProperty("servers")
    public abstract List<ServerConfigs> serversConfigs();

    @JsonCreator
    public static ConnectServerConfigs create(
            @JsonProperty("hostname") String hostname,
            @JsonProperty("tcpPort") Integer tcpPort,
            @JsonProperty("udpPort") Integer udpPort,
            @JsonProperty("servers") List<ServerConfigs> serversConfigs
    ) {
        return builder()
                .hostname(hostname)
                .tcpPort(tcpPort)
                .udpPort(udpPort)
                .serversConfigs(serversConfigs)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder tcpPort(Integer tcpPort);

        public abstract Builder udpPort(Integer udpPort);

        public abstract Builder hostname(String hostname);

        public abstract Builder serversConfigs(List<ServerConfigs> serversConfigs);

        public abstract ConnectServerConfigs build();
    }
}
