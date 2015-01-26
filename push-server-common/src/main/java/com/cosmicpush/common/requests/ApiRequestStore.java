/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.common.requests;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.PushType;
import com.couchace.core.api.query.CouchViewQuery;
import java.util.List;
import org.crazyyak.dev.couchace.DefaultCouchStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiRequestStore extends DefaultCouchStore<ApiRequest> {

  public static final String API_REQUEST_DESIGN_NAME = "api-request";

  @Autowired
  public ApiRequestStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), ApiRequest.class);
  }

  public ApiRequest getByApiRequestId(String apiRequestId) {
    return super.getByDocumentId(apiRequestId);
  }

  public QueryResult<ApiRequest> getByClient(ApiClient apiClient, int limit) {

    CouchViewQuery viewQuery = CouchViewQuery.builder(getDesignName(), "byClient")
        .limit(limit)
        .key(apiClient.getApiClientId())
        .build();

    return new QueryResult<>(ApiRequest.class, database, viewQuery);
  }

  public List<ApiRequest> getByClientAndType(ApiClient apiClient, PushType type) {
    return super.getEntities("byClientAndType", apiClient.getApiClientId(), type.getCode());
  }

  public List<ApiRequest> getByClientAndSession(ApiClient apiClient, String sessionId) {
    return super.getEntities("byClientAndSession", apiClient.getApiClientId(), sessionId);
  }

  public List<ApiRequest> getByClientAndDevice(ApiClient apiClient, String deviceId) {
    return super.getEntities("byClientAndDevice", apiClient.getApiClientId(), deviceId);
  }

  @Override
  public String getDesignName() {
    return API_REQUEST_DESIGN_NAME;
  }

}
