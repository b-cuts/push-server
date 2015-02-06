package com.cosmicpush.app.system;

import org.apache.log4j.Logger;
import org.crazyyak.app.logging.LogUtils;
import org.crazyyak.lib.jaxrs.YakJaxRsExceptionMapper;

public class CpJaxRsExceptionMapper extends YakJaxRsExceptionMapper {

  public CpJaxRsExceptionMapper() {
    super(true);
  }

  @Override
  protected void logInfo(String msg, Throwable ex) {
    Logger.getLogger(CpJaxRsExceptionMapper.class).info(msg, ex);
  }

  @Override
  protected void logError(String msg, Throwable ex) {
    Logger.getLogger(CpJaxRsExceptionMapper.class).fatal(msg, ex);
  }
}
