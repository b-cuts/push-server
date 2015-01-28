package com.cosmicpush.common.requests;

import com.couchace.core.api.*;
import com.couchace.core.api.http.CouchHttpException;
import com.couchace.core.api.query.*;
import com.couchace.core.api.response.*;
import java.util.List;

public class QueryResult<T> {

  private final Class<T> type;
  private final CouchDatabase database;
  private GetEntityResponse<T> response;

  public QueryResult(Class<T> type, CouchDatabase database, CouchViewQuery viewQuery) {
    this.type = type;
    this.database = database;
    validate(this.response = database.get().entity(type, viewQuery).execute());
  }

  public List<T> getEntityList() {
    return response.getEntityList();
  }

  public List<EntityDocument<T>> getDocumentList() {
    return response.getDocumentList();
  }

  public int getSize() {
    return response.getSize();
  }

  public boolean hasNextPage() {
    return response.getCouchPageNavigation().hasNextPage();
  }

  public boolean nextPage() {
    boolean hasNext = hasNextPage();
    if (hasNext) {
      CouchPageNavigation pageNavigation = response.getCouchPageNavigation();
      CouchPageQuery nextPageQuery = pageNavigation.queryNextPage();
      validate(this.response = database.get().entity(type, nextPageQuery).execute());
    }
    return hasNext;
  }

  public boolean hasPrevPage() {
    return response.getCouchPageNavigation().hasPreviousPage();
  }

  public boolean prevPage() {
    boolean hasPrev = hasPrevPage();
    if (hasPrev) {
      CouchPageNavigation pageNavigation = response.getCouchPageNavigation();
      CouchPageQuery prevPageQuery = pageNavigation.queryPreviousPage();
      validate(this.response = database.get().entity(type, prevPageQuery).execute());
    }
    return hasPrev;
  }

  private static void validate(GetEntityResponse response) {
    if (response.isOk() == false) {
      throw new CouchHttpException(response.getHttpStatus(), "Unexpected response while executing couch query.");
    }
  }
}
