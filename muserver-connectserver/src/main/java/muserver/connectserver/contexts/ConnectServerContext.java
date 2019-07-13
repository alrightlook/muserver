package muserver.connectserver.contexts;

import muserver.common.configs.ServerConfigs;

import java.util.Map;

public class ConnectServerContext {
 private final Map<Short, ServerConfigs> serversConfigsMap;

 public ConnectServerContext(Map<Short, ServerConfigs> serversConfigsMap) {
  this.serversConfigsMap = serversConfigsMap;
 }

 public Map<Short, ServerConfigs> serversConfigsMap() {
  return serversConfigsMap;
 }
}