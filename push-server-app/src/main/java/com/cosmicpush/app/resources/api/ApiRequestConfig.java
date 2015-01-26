/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.domain.clients.ApiClientUser;
import com.cosmicpush.app.resources.RequestConfig;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import javax.servlet.http.*;
import javax.ws.rs.core.*;
import org.springframework.security.authentication.AuthenticationManager;

public class ApiRequestConfig extends RequestConfig {

  public ApiRequestConfig(AuthenticationManager authenticationManager, CpObjectMapper objectMapper, CpCouchServer couchServer, AccountStore accountStore, ApiRequestStore apiRequestStore) {
    super(authenticationManager, objectMapper, couchServer, accountStore, apiRequestStore);
  }

  @Override
  public ApiRequestConfig initialize(HttpServletRequest request, HttpServletResponse response, UriInfo uriInfo, HttpHeaders headers, SecurityContext securityContext) {
    super.initialize(request, response, uriInfo, headers, securityContext);
    return this;
  }

  public ApiClientUser getApiClientUser() {
    return (ApiClientUser)userDetails;
  }
}
