/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import com.cosmicpush.app.deprecated.SmsToEmailPush;
import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.SessionStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.*;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.ApplicationPath;
import org.crazyyak.app.logging.LogUtils;
import org.crazyyak.lib.jaxrs.YakJaxRsExceptionMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class CpApplication extends ResourceConfig {

  private static final ThreadLocal<ExecutionContext> executionContext = new ThreadLocal<>();
  public static ExecutionContext getExecutionContext() {
    if (executionContext.get() == null) {
      executionContext.set(new ExecutionContext());
    }
    return executionContext.get();
  }
  public static void removeExecutionContext() {
    executionContext.remove();
  }

  public CpApplication() {
    new LogUtils().initConsoleAppender();


    String databaseName = "cosmic-push";

    AppContext appContext = new AppContext(
      new SessionStore(TimeUnit.MINUTES.toMillis(60)),
      new CpObjectMapper(),
      new CpCouchServer(databaseName));
    property(AppContext.class.getName(), appContext);

    register(CpFilter.class);
    register(CpReaderWriterProvider.class);
    register(MultiPartFeature.class);
    register(YakJaxRsExceptionMapper.class);
    register(new CpReaderWriterProvider(appContext.getObjectMapper()));

    packages("com.cosmicpush");

    // Force a reference to this deprecated push.
    new PushType(GoogleTalkPush.class, "im", "Deprecated IM Push");
    new PushType(SmtpEmailPush.class, "email", "Deprecated Email Push");
    SmsToEmailPush.PUSH_TYPE.getCode();
  }
}
