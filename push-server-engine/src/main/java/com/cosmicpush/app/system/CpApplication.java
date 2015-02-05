/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import com.cosmicpush.app.deprecated.EmailToSmsPush;
import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.ApiAuthenticationFilter;
import com.cosmicpush.app.jaxrs.security.MngtAuthenticationFilter;
import com.cosmicpush.app.jaxrs.security.SessionFilter;
import com.cosmicpush.app.jaxrs.security.SessionStore;
import com.cosmicpush.app.resources.RootResource;
import com.cosmicpush.app.view.LocalResourceMessageBodyWriter;
import com.cosmicpush.app.view.ThymeleafMessageBodyWriter;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.GoogleTalkPush;
import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.pub.push.SmtpEmailPush;
import com.cosmicpush.pub.push.UserEventPush;
import org.apache.log4j.Level;
import org.crazyyak.app.logging.LogUtils;
import org.crazyyak.lib.jaxrs.YakJaxRsExceptionMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.core.Application;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CpApplication extends Application {

  private static final ThreadLocal<ExecutionContext> executionContext = new ThreadLocal<>();
  public static boolean hasExecutionContext() {
    return executionContext.get() != null;
  }
  public static ExecutionContext getExecutionContext() {
    if (executionContext.get() == null) {
      executionContext.set(new ExecutionContext());
    }
    return executionContext.get();
  }
  public static void removeExecutionContext() {
    executionContext.remove();
  }

  private final Set<Class<?>> classes;
  private final Set<Object> singletons;
  private final Map<String, Object> properties;

  public CpApplication() throws Exception {
    Map<String, Object> properties = new HashMap<>();
    Set<Class<?>> classes = new HashSet<>();
    Set<Object> singletons = new HashSet<>();

    LogUtils logUtils = new LogUtils();
    logUtils.initConsoleAppender(Level.WARN, LogUtils.DEFAULT_PATTERN);

    String databaseName = "cosmic-push";

    AppContext appContext = new AppContext(
      new SessionStore(TimeUnit.MINUTES.toMillis(60)),
      new CpObjectMapper(),
      new CpCouchServer(databaseName));
    properties.put(AppContext.class.getName(), appContext);

    getSingletons();

    classes.add(CpFilter.class);
    classes.add(SessionFilter.class);
    classes.add(MultiPartFeature.class);
    classes.add(ApiAuthenticationFilter.class);
    classes.add(MngtAuthenticationFilter.class);
    classes.add(ThymeleafMessageBodyWriter.class);
    classes.add(LocalResourceMessageBodyWriter.class);
    classes.add(RootResource.class);

    singletons.add(new CpReaderWriterProvider(appContext.getObjectMapper()));
    singletons.add(new YakJaxRsExceptionMapper(true) {
      @Override protected void logInfo(String msg, Throwable ex) {
        logUtils.info(CpApplication.class, msg, ex);
      }
      @Override protected void logError(String msg, Throwable ex) {
        logUtils.fatal(CpApplication.class, msg, ex);
      }
    });

    // TODO - remove these once these are properly referenced by their plugins
    UserEventPush.PUSH_TYPE.getCode();
    NotificationPush.PUSH_TYPE.getCode();
    new PushType(GoogleTalkPush.class, "im", "IM");
    new PushType(SmtpEmailPush.class, "email", "eMail");
    new PushType(EmailToSmsPush.class, "emailToSms", "eMail->SMS");

    this.classes = Collections.unmodifiableSet(classes);
    this.singletons = Collections.unmodifiableSet(singletons);
    this.properties = Collections.unmodifiableMap(properties);

    checkForDuplicates();
  }

  private void checkForDuplicates() {
    Set<Class> existing = new HashSet<>();

    for (Object object : singletons) {
      if (object == null) continue;
      if (object instanceof Class) {
        String msg = String.format("The class %s was registered as a singleton.", Class.class.getName());
        throw new IllegalArgumentException(msg);

      } else if (existing.contains(object.getClass())) {
        String msg = String.format("The singleton %s has already been registered.", object.getClass().getName());
        throw new IllegalArgumentException(msg);
      }
    }

    for (Class type : classes) {
      if (type == null) continue;
      if (existing.contains(type)) {
        String msg = String.format("The class %s has already been registered.", type.getName());
        throw new IllegalArgumentException(msg);
      }
    }

    existing.clear();
  }

  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }
  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }
  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }
}
