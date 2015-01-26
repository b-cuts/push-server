/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage;

import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.app.resources.RequestConfig;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import javax.servlet.http.*;
import javax.ws.rs.core.*;
import org.springframework.security.authentication.AuthenticationManager;

public class UserRequestConfig extends RequestConfig {

  public UserRequestConfig(AuthenticationManager authenticationManager, CpObjectMapper objectMapper, CpCouchServer couchServer, AccountStore accountStore, ApiRequestStore apiRequestStore) {
    super(authenticationManager, objectMapper, couchServer, accountStore, apiRequestStore);
  }

  @Override
  public UserRequestConfig initialize(HttpServletRequest request, HttpServletResponse response, UriInfo uriInfo, HttpHeaders headers, SecurityContext securityContext) {
    super.initialize(request, response, uriInfo, headers, securityContext);
    return this;
  }

  public AccountUser getCurrentUser() {
    return (AccountUser)userDetails;
  }
}
