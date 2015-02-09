/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.common.requests;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.PushType;
import com.couchace.core.api.query.CouchViewQuery;
import java.util.List;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class ApiRequestStore extends DefaultCouchStore<ApiRequest> {

  public static final String API_REQUEST_DESIGN_NAME = "api-request";

  public ApiRequestStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), ApiRequest.class);
  }

  public ApiRequest getByApiRequestId(String apiRequestId) {
    return super.getByDocumentId(apiRequestId);
  }

  public QueryResult<ApiRequest> getByClient(Domain domain, int limit) {

    CouchViewQuery viewQuery = CouchViewQuery.builder(getDesignName(), "byClient")
        .limit(limit)
        .key(domain.getDomainId())
        .build();

    return new QueryResult<>(ApiRequest.class, database, viewQuery);
  }

  public List<ApiRequest> getByClientAndType(Domain domain, PushType type) {
    return super.getEntities("byClientAndType", domain.getDomainId(), type.getCode());
  }

  public List<ApiRequest> getByClientAndSession(Domain domain, String sessionId) {
    return super.getEntities("byClientAndSession", domain.getDomainId(), sessionId);
  }

  public List<ApiRequest> getByClientAndDevice(Domain domain, String deviceId) {
    return super.getEntities("byClientAndDevice", domain.getDomainId(), deviceId);
  }

  @Override
  public String getDesignName() {
    return API_REQUEST_DESIGN_NAME;
  }

}
