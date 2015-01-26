package com.cosmicpush.common.system;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.pub.common.PushType;
import java.util.*;

public class PluginManager {

  private static final PluginManager SINGLETON = new PluginManager();

  private final Map<PushType,Plugin> map = new HashMap<>();

  private PluginManager() {

    ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
    loader.reload();

    for (Plugin plugin : loader) {
      PushType pushType = plugin.getPushType();
      map.put(pushType, plugin);
      PushType.register(pushType);
    }

    int expectedCount = 3;
    if (map.size() < expectedCount) {
      String msg = String.format("Expected at least %s plugins but only found %s %s", expectedCount, map.size(), map.keySet());
      throw new IllegalStateException(msg);
    }
  }

  public static List<Plugin> getPlugins() {
    return new ArrayList<>(SINGLETON.map.values());
  }

  public static Plugin getPlugin(PushType pushType) {
    if (SINGLETON.map.containsKey(pushType) == false) {
      String msg = String.format("The plugin for \"%s\" was not found.", pushType.getCode());
      throw new IllegalArgumentException(msg);
    }
    return SINGLETON.map.get(pushType);
  }

  public static <T extends Plugin> T getPlugin(Class<T> type) {
    for (Plugin plugin : SINGLETON.map.values()) {
      if (type.equals(plugin.getClass())) {
        // noinspection unchecked
        return (T)plugin;
      }
    }
    String msg = String.format("The plugin for \"%s\" was not found.", type.getName());
    throw new IllegalArgumentException(msg);
  }

  public static PluginConfig getConfig(PluginContext context, ApiClient apiClient, PushType pushType) {
    Plugin plugin = getPlugin(pushType);
    return plugin.getConfig(context.getCouchServer(), apiClient);
  }
}
