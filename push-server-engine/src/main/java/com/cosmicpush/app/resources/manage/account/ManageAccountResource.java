/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.account;

import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.actions.UpdateAccountAction;
import com.cosmicpush.common.clients.Domain;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@MngtAuthentication
public class ManageAccountResource {

  private final Account account;

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageAccountResource(Account account) {
    this.account = account;
  }

  @GET
  public Thymeleaf viewAccount() throws IOException {
    ExecutionContext execContext = CpApplication.getExecutionContext();
    Account account = execContext.getAccount();
    List<Domain> domains = execContext.getDomainStore().getDomains(account);

    ManageAccountModel model = new ManageAccountModel(account, domains);
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
