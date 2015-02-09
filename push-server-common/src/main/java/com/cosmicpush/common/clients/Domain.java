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

@CouchEntity(DomainStore.DOMAIN_DESIGN_NAME)
public class Domain {

  private String domainId;
  private String revision;

  private String accountId;

  private String domainKey;
  private String domainPassword;

  @JsonCreator
  public Domain(@JacksonInject("domainId") String domainId,
                @JacksonInject("revision") String revision,
                @JsonProperty("accountId") String accountId,
                @JsonProperty("domainKey") String domainKey,
                @JsonProperty("domainPassword") String domainPassword) {

    this.domainId = domainId;
    this.revision = revision;
    this.accountId = accountId;
    this.domainKey = domainKey;
    this.domainPassword = domainPassword;
  }


  public Domain (CreateDomainAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    this.domainId = CpIdGenerator.newId();
    this.accountId = action.getAccountId();
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

  public String getAccountId() {
    return accountId;
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
