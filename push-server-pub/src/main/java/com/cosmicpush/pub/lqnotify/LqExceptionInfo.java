package com.cosmicpush.pub.lqnotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LqExceptionInfo {

  private final String exceptionType;
  private final String message;
  private final List<LqStackTraceElement> stackTrace;
  private final LqExceptionInfo cause;

  private LqExceptionInfo(@JsonProperty("exceptionType") String exceptionType,
                          @JsonProperty("message") String message,
                          @JsonProperty("stackTrace") List<LqStackTraceElement> stackTrace,
                          @JsonProperty("cause") LqExceptionInfo cause) {

    this.exceptionType = (exceptionType != null) ? exceptionType : "undefined";
    this.message = (message != null) ? message : "none";
    this.stackTrace = (stackTrace != null) ? Collections.unmodifiableList(stackTrace) : Collections.emptyList();
    this.cause = cause;
  }

  public String getExceptionType() {
      return exceptionType;
  }

  public String getMessage() {
      return message;
  }

  public List<LqStackTraceElement> getStackTrace() {
      return stackTrace;
  }

  public LqExceptionInfo getCause() {
      return cause;
  }

  public static LqExceptionInfo create(Throwable t) {
    String exceptionType = t.getClass().getName();
    String message = t.getMessage();
    List<LqStackTraceElement> stackTraces = new ArrayList<>();
    for(StackTraceElement element : t.getStackTrace()) {
      stackTraces.add(new LqStackTraceElement(element));
    }

    LqExceptionInfo cause = null;
    if (t.getCause() != null) {
      cause = LqExceptionInfo.create(t.getCause());
    }

    return new LqExceptionInfo(exceptionType, message, stackTraces, cause);
  }
}
