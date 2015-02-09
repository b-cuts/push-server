/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.resources.manage.client.emails.ManageEmailsResource;
import com.cosmicpush.app.resources.manage.client.notifications.ManageNotificationsResource;
import com.cosmicpush.app.resources.manage.client.userevents.ManageUserEventsResource;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.DomainStore;
import com.cosmicpush.common.actions.UpdateDomainAction;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.requests.PushRequestStore;
import com.cosmicpush.common.requests.QueryResult;
import com.cosmicpush.pub.common.PushType;
import org.crazyyak.dev.common.exceptions.ApiException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MngtAuthentication
public class ManageDomainResource {

  private final ExecutionContext execContext = CpApplication.getExecutionContext();
  private final String domainKey;

  public ManageDomainResource(String domainKey) {
    this.domainKey = domainKey;
  }

  private Account getAccount() {
    return execContext.getAccount();
  }

  private Domain getDomain() {
    DomainStore domainStore = execContext.getDomainStore();
    Domain domain = domainStore.getByDomainKey(domainKey);
    Account account = getAccount();

    if (domain == null) {
      throw ApiException.notFound(domainKey);
    } else if (account.isNotOwner(domain)) {
      throw ApiException.forbidden();
    }
    return domain;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewDomain() throws Exception {
    Domain domain = getDomain();

    String lastMessage = execContext.getSession().getLastMessage();
    execContext.getSession().setLastMessage(null);
    execContext.getAccountStore().update(getAccount());

    ManageDomainModel model = new ManageDomainModel(execContext, getAccount(), domain, lastMessage);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_CLIENT, model);
  }

  @GET
  @Path("/requests")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEvents() throws Exception {
    Domain domain = getDomain();

    QueryResult<PushRequest> queryResult = execContext.getPushRequestStore().getByClient(domain, 500);
    List<PushRequest> requests = new ArrayList<>(queryResult.getEntityList());

    Collections.sort(requests);
    Collections.reverse(requests);

    DomainRequestsModel model = new DomainRequestsModel(getAccount(), domain, requests);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_REQUESTS, model);
  }

  @POST
  @Path("/requests/delete-all")
  public Response deleteEvents() throws Exception {
    Domain domain = getDomain();
    PushRequestStore requestStore = execContext.getPushRequestStore();

    QueryResult<PushRequest> queryResult = requestStore.getByClient(domain, 500);
    List<PushRequest> requests = new ArrayList<>(queryResult.getEntityList());

    for (PushRequest request : requests) {
      requestStore.delete(request);
    }

    execContext.getSession().setLastMessage("All API Requests deleted");
    execContext.getDomainStore().update(domain);

    URI uri = execContext.getUriInfo().getBaseUriBuilder().path("manage").path("domain").path(domain.getDomainKey()).path("requests").build();
    return Response.seeOther(uri).build();
  }


  @Path("/emails")
  public ManageEmailsResource getManageEmailsResource() throws Exception {
    return new ManageEmailsResource(getAccount(), getDomain());
  }

  @Path("/user-events")
  public ManageUserEventsResource getManageUserEventsResource() throws Exception {
    return new ManageUserEventsResource(getAccount(), getDomain());
  }

  @Path("/notifications")
  public ManageNotificationsResource getManageNotificationsResource() throws Exception {
    return new ManageNotificationsResource(getAccount(), getDomain());
  }

  @POST
  @Path("/client")
  public Response updateClient(@FormParam("domainKey") String domainKey, @FormParam("domainPassword") String domainPassword) throws Exception {

    Domain domain = getDomain();
    DomainStore domainStore = execContext.getDomainStore();

    if (domain.getDomainKey().equals(domainKey) == false && domainStore.getByDomainKey(domainKey) != null) {
      // The specified name is not the same as the current value but it is already in use by another account.
      String msg = String.format("The client name %s already exists.", domainKey);
      throw ApiException.badRequest(msg);
    }

    UpdateDomainAction action = new UpdateDomainAction(domainKey, domainPassword);
    execContext.getSession().setLastMessage("Domain configuration changed.");

    domain.apply(action);
    domainStore.update(domain);

    URI uri = execContext.getUriInfo().getBaseUriBuilder().path("manage").path("domain").path(domain.getDomainKey()).build();
    return Response.seeOther(uri).build();
  }

  @POST
  @Path("/client/delete")
  public Response deleteClient() throws Exception {

    Domain domain = getDomain();
    Account account = getAccount();

    getAccount().remove(domain);
    execContext.getDomainStore().delete(domain);
    execContext.getAccountStore().update(account);

    return Response.seeOther(new URI("manage/account")).build();
  }

  @Path("/{pushType}")
  public ManagePluginApi getManagePluginApi(@PathParam("pushType") PushType pushType) throws Exception {
    return new ManagePluginApi(getAccount(), getDomain(), pushType);
  }
}