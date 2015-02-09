package com.cosmicpush.pub.lqnotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LqStackTraceElement {

  private final String className;
  private final String methodName;
  private final String fileName;
  private final int lineNumber;

  public LqStackTraceElement(@JsonProperty("className") String className,
                             @JsonProperty("methodName") String methodName,
                             @JsonProperty("fileName") String fileName,
                             @JsonProperty("lineNumber") int lineNumber) {

    this.className = className;
    this.methodName = methodName;
    this.fileName = fileName;
    this.lineNumber = lineNumber;
  }

  public LqStackTraceElement(StackTraceElement element) {
    this.className = element.getClassName();
    this.methodName = element.getMethodName();
    this.fileName = element.getFileName();
    this.lineNumber = element.getLineNumber();
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getFileName() {
    return fileName;
  }

  public int getLineNumber() {
    return lineNumber;
  }
}
