package com.cosmicpush.jackson;

import com.fasterxml.jackson.databind.*;
import java.util.*;
import org.crazyyak.dev.jackson.*;

public class CpObjectMapper extends YakJacksonObjectMapper {

  public CpObjectMapper() {
    super(Arrays.asList(
                new YakJacksonModule(),
                new CpJacksonModule()),
        Collections.<YakJacksonInjectable>emptyList());
  }

  protected CpObjectMapper(Collection<? extends Module> modules, Collection<? extends YakJacksonInjectable> injectables) {
    super(modules, injectables);
  }

  @Override
  public ObjectMapper copy() {
    _checkInvalidCopy(CpObjectMapper.class);
    return new CpObjectMapper(getModules(), getInjectables());
  }

}
