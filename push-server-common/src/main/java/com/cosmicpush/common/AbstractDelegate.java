/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common;

import com.cosmicpush.common.requests.*;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.RequestStatus;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

public abstract class AbstractDelegate implements Runnable {

  protected abstract RequestStatus processRequest() throws Exception;

  protected final CpObjectMapper objectMapper;
  protected final ApiRequest apiRequest;
  protected final ApiRequestStore apiRequestStore;

  protected AbstractDelegate(CpObjectMapper objectMapper, ApiRequest apiRequest, ApiRequestStore apiRequestStore) {
    this.objectMapper = ExceptionUtils.assertNotNull(objectMapper, "objectMapper");
    this.apiRequest = ExceptionUtils.assertNotNull(apiRequest, "apiRequest");
    this.apiRequestStore = ExceptionUtils.assertNotNull(apiRequestStore, "apiRequestStore");
  }

  @Override
  public final void run() {
    execute(false);
  }

  public void retry() {
    execute(true);
  }

  private void execute(boolean retry) {

    if (retry) {
      apiRequest.addNote("** WARNING ** the API Request is being reprocessed.");
    }

    try {
      processRequest();
    } catch (Exception ex) {
      ex.printStackTrace();
      apiRequest.failed(ex);
    }

    try {
      processCallback();
    } catch (Exception ex) {
      ex.printStackTrace();
      apiRequest.warn(ex);
    }

    apiRequestStore.update(apiRequest);
  }

  private void processCallback() throws Exception {

    String callbackURL = apiRequest.getPush().getCallbackUrl();
    if (callbackURL == null) {
      apiRequest.addNote("Callback not processed - url not specified");
      return;
    }

    String userName = getUserName(callbackURL);
    String password = getPassword(callbackURL);
    callbackURL = stripAuthentication(callbackURL);

    Client client = ClientBuilder.newBuilder().build();
    UriBuilder uriBuilder = new JerseyUriBuilder().uri(callbackURL);

    apiRequest.addNote("Executing callback to " + callbackURL);

    String json = objectMapper.writeValueAsString(apiRequest);
    Invocation.Builder builder;

    if (userName != null) {
      builder = client.target(uriBuilder)
          .register(HttpAuthenticationFeature.basic(userName, password))
          .request(MediaType.APPLICATION_JSON_TYPE);

    } else {
      builder = client.target(uriBuilder)
          .request(MediaType.APPLICATION_JSON_TYPE);
    }

    Response jerseyResponse = builder.post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
    int status = jerseyResponse.getStatus();

    if (status / 100 == 2) {
      apiRequest.addNote("Callback completed: HTTP " + status);
    } else {
      apiRequest.warn("Callback failed: HTTP " + status);
    }
  }

  public static String getUserName(String callbackURL) {
    if (StringUtils.isBlank(callbackURL)) return null;

    int posA = callbackURL.indexOf("://");
    if (posA < 0) return null;

    int posB = callbackURL.indexOf("@", posA);
    if (posB < 0) return null;

    String auth = callbackURL.substring(posA+3, posB);
    int pos = auth.indexOf(":");

    return (pos < 0) ? auth : auth.substring(0, pos);
  }

  public static String getPassword(String callbackURL) {
    if (StringUtils.isBlank(callbackURL)) return null;

    int posA = callbackURL.indexOf("://");
    if (posA < 0) return null;

    int posB = callbackURL.indexOf("@", posA);
    if (posB < 0) return null;

    String auth = callbackURL.substring(posA+3, posB);
    int pos = auth.indexOf(":");

    return (pos < 0) ? null : auth.substring(pos+1);
  }

  public static String stripAuthentication(String callbackURL) {
    if (StringUtils.isBlank(callbackURL)) return callbackURL;

    int posA = callbackURL.indexOf("://");
    if (posA < 0) return callbackURL;

    int posB = callbackURL.indexOf("@", posA);
    if (posB < 0) return callbackURL;

    String left = callbackURL.substring(0, posA+3);
    String right = callbackURL.substring(posB+1);

    return left+right;
  }

  public void start() {
    new Thread(this).start();
  }
}
