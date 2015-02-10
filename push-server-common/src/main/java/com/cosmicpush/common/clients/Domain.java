/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.clients;

import com.cosmicpush.common.accounts.DomainStore;
import com.cosmicpush.common.actions.CreateDomainAction;
import com.cosmicpush.common.actions.UpdateDomainAction;
import com.cosmicpush.pub.internal.CpIdGenerator;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.CouchEntity;
import com.couchace.annotations.CouchId;
import com.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@CouchEntity(DomainStore.DOMAIN_DESIGN_NAME)
public class Domain {

  private String domainId;
  private String revision;

  private String domainKey;
  private String domainPassword;

  private int retentionDays;

  private final List<String> accountIds = new ArrayList<>();

  @JsonCreator
  public Domain(@JacksonInject("domainId") String domainId,
                @JacksonInject("revision") String revision,
                @JsonProperty("domainKey") String domainKey,
                @JsonProperty("domainPassword") String domainPassword,
                @JsonProperty("retentionDays") int retentionDays,
                @JsonProperty("accountIds") List<String> accountIds) {

    this.domainId = domainId;
    this.revision = revision;

    this.domainKey = domainKey;
    this.domainPassword = domainPassword;

    this.retentionDays = retentionDays;

    if (accountIds != null) {
      this.accountIds.addAll(accountIds);
    }
  }

  public Domain (CreateDomainAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    this.retentionDays = 7;
    this.domainId = CpIdGenerator.newId();
    this.accountIds.add(action.getAccountId());
    this.domainKey = action.getDomainKey();
    this.domainPassword = action.getDomainPassword();
  }

  @CouchId
  public String getDomainId() {
    return domainId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public int getRetentionDays() {
    return retentionDays;
  }

  public List<String> getAccountIds() {
    return accountIds;
  }

  public String getDomainKey() {
    return domainKey;
  }

  public String getDomainPassword() {
    return domainPassword;
  }

  public void apply(UpdateDomainAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    this.domainKey = action.getDomainKey();
    this.domainPassword = action.getDomainPassword();
    this.retentionDays = action.getRetentionDays();
  }

  public boolean equals(Object object) {
    if (object instanceof Domain) {
      Domain that = (Domain)object;
      return this.domainId.equals(that.domainId);
    }
    return false;
  }

  public String toString() {
    return domainKey;
  }
}
