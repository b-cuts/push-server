/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.system;

import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.*;
import java.util.Arrays;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import org.crazyyak.lib.jaxrs.jackson.JacksonReaderWriterProvider;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CpReaderWriterProvider extends JacksonReaderWriterProvider {

  public CpReaderWriterProvider(@Context Application application) {
    super(AppContext.from(application).getObjectMapper(), Arrays.asList(MediaType.APPLICATION_JSON_TYPE));

    super.supportedTypes.add(Push.class);
    super.supportedTypes.add(UserAgent.class);
    super.supportedTypes.add(PushResponse.class);
  }
}
