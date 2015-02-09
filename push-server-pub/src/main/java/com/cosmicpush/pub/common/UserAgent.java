/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosmicpush.pub.common;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.StringUtils;

import java.io.Serializable;

public class UserAgent implements Serializable {

  private final String agentType;
  private final String agentName;
  private final String agentVersion;
  private final String agentLanguage;
  private final String agentLanguageTag;
  private final String osType;
  private final String osName;
  private final String osProducer;
  private final String osProducerUrl;
  private final String osVersionName;
  private final String osVersionNumber;
  private final String linuxDistribution;

  @JsonCreator
  public UserAgent(@JsonProperty("agentType") String agentType,
                    @JsonProperty("agentName") String agentName,
                    @JsonProperty("agentVersion") String agentVersion,
                    @JsonProperty("agentLanguage") String agentLanguage,
                    @JsonProperty("agentLanguageTag") String agentLanguageTag,
                    @JsonProperty("osType") String osType,
                    @JsonProperty("osName") String osName,
                    @JsonProperty("osProducer") String osProducer,
                    @JsonProperty("osProducerUrl") String osProducerUrl,
                    @JsonProperty("osVersionName") String osVersionName,
                    @JsonProperty("osVersionNumber") String osVersionNumber,
                    @JsonProperty("linuxDistribution") String linuxDistribution) {

    this.agentType = agentType;
    this.agentName = agentName;
    this.agentVersion = agentVersion;
    this.agentLanguage = agentLanguage;
    this.agentLanguageTag = agentLanguageTag;
    this.osType = osType;
    this.osName = osName;
    this.osProducer = osProducer;
    this.osProducerUrl = osProducerUrl;
    this.osVersionName = osVersionName;
    this.osVersionNumber = osVersionNumber;
    this.linuxDistribution = linuxDistribution;
  }

  public String getAgentType() {
    return agentType;
  }
  public String getAgentName() {
    return agentName;
  }
  public String getAgentVersion() {
    return agentVersion;
  }
  public String getAgentLanguage() {
    return agentLanguage;
  }
  public String getAgentLanguageTag() {
    return agentLanguageTag;
  }
  public String getOsType() {
    return osType;
  }
  public String getOsName() {
    return osName;
  }
  public String getOsProducer() {
    return osProducer;
  }
  public String getOsProducerUrl() {
    return osProducerUrl;
  }
  public String getOsVersionName() {
    return osVersionName;
  }
  public String getOsVersionNumber() {
    return osVersionNumber;
  }
  public String getLinuxDistribution() {
    return linuxDistribution;
  }

  @JsonIgnore
  public String getOs() {
    String retVal = "";
    if (osName != null) retVal = (retVal + " " + osName).trim();
    if (osVersionNumber != null) retVal = (retVal + " " + osVersionNumber).trim();
    if (osVersionName != null) retVal = (retVal + " " + osVersionName).trim();

    return StringUtils.isBlank(retVal) ? null : retVal;
  }

  @JsonIgnore
  public String getAgent() {
    String retVal = "";
    if (agentName != null) retVal = (retVal + " " + agentName).trim();
    if (agentVersion != null) retVal = (retVal + " " + agentVersion).trim();
    if (agentLanguage != null) retVal = (retVal + " " + agentLanguage).trim();

    return StringUtils.isBlank(retVal) ? null : retVal;
  }
}
