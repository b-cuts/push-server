/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import com.cosmicpush.app.deprecated.SmsToEmailPush;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.*;
import org.apache.commons.logging.*;
import org.crazyyak.dev.jerseyspring.*;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class CpApplication extends ResourceConfig {

  private static final Log log = LogFactory.getLog(CpApplication.class);

  public CpApplication() {
    log.info("Application loaded.");

    property(YakJspMvcFeature.SUPPORTED_EXTENSIONS, "jsp, jspf");

    register(CpFilter.class);
    register(CpReaderWriterProvider.class);
    register(MultiPartFeature.class);
    register(YakJspMvcFeature.class);
    register(YakExceptionMapper.class);

    packages("com.cosmicpush");

    // Force a reference to this deprecated push.
    new PushType(GoogleTalkPush.class, "im", "Deprecated IM Push");
    new PushType(SmtpEmailPush.class, "email", "Deprecated Email Push");
    SmsToEmailPush.PUSH_TYPE.getCode();
  }
}
