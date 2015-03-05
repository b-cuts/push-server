/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.*;
import com.fasterxml.jackson.annotation.*;
import org.crazyyak.dev.common.EqualsUtils;

import java.io.Serializable;

@CouchEntity(LqNotificationsConfigStore.NOTIFIER_CONFIG_DESIGN_NAME)
public class LqNotificationsConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String domainId;

  private String userName;

  public LqNotificationsConfig() {
  }

  @JsonCreator
  public LqNotificationsConfig(@JacksonInject("configId") String configId,
                               @JacksonInject("revision") String revision,
                               @JsonProperty("domainId") String domainId,
                               @JsonProperty("userName") String userName) {

    this.configId = configId;
    this.revision = revision;
    this.domainId = domainId;
    this.userName = userName;
  }

  @CouchId
  public String getConfigId() {
    return configId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public String getDomainId() {
    return domainId;
  }

  public String getUserName() {
    return userName;
  }

  public LqNotificationsConfig apply(UpdateLqNotificationsConfigAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    if (domainId != null && EqualsUtils.objectsNotEqual(domainId, action.getDomain().getDomainId())) {
      String msg = "The specified push and this class are not for the same domain.";
      throw new IllegalArgumentException(msg);
    }

    this.domainId = action.getDomain().getDomainId();
    this.configId = LqNotificationsConfigStore.toDocumentId(action.getDomain());

    this.userName = action.getUserName();

    return this;
  }
}
