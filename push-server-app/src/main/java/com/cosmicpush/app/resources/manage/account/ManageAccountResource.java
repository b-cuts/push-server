/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.account;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.actions.UpdateAccountAction;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@MngtAuthentication
public class ManageAccountResource {

  private final Account account;

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageAccountResource(Account account) {
    this.account = account;
  }

  @GET
  public Thymeleaf viewAccount() throws IOException {
    Account account = CpApplication.getExecutionContext().getAccount();
    ManageAccountModel model = new ManageAccountModel(account);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_ACCOUNT, model);
  }

  @POST
  public Response updateAccount(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("emailAddress") String emailAddress) throws Exception {

    UpdateAccountAction action = new UpdateAccountAction(firstName, lastName, emailAddress);
    account.apply(action);
    context.getAccountStore().update(account);

    return Response.seeOther(new URI("manage/account")).build();
  }
}
