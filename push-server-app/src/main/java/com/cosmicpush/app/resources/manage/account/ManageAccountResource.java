/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.account;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.actions.UpdateAccountAction;
import com.cosmicpush.app.resources.manage.UserRequestConfig;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.mvc.Viewable;

public class ManageAccountResource {

  private final Account account;

  private final UserRequestConfig config;

  public ManageAccountResource(UserRequestConfig config) {
    this.config = config;

    String accountId = config.getCurrentUser().getAccountId();
    this.account = config.getAccountStore().getByAccountId(accountId);
  }

  @GET
  public Viewable viewAccount() throws IOException {
    ManageAccountModel model = new ManageAccountModel(account);
    return new Viewable("/manage/account.jsp", model);
  }

  @POST
  public Response updateAccount(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("emailAddress") String emailAddress) throws Exception {

    UpdateAccountAction action = new UpdateAccountAction(firstName, lastName, emailAddress);
    account.apply(action);
    config.getAccountStore().update(account);

    return Response.seeOther(new URI("manage/account")).build();
  }
}
