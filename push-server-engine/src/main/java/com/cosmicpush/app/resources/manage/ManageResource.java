/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.resources.manage.account.ManageAccountResource;
import com.cosmicpush.app.resources.manage.client.ManageApiClientResource;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.actions.CreateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.PushType;
import org.crazyyak.dev.common.exceptions.ApiException;
import org.crazyyak.dev.common.net.InetMediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

@MngtAuthentication
public class ManageResource {

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageResource() {
  }

  @GET
  public Response redirect() throws Exception {
    return Response.seeOther(new URI("manage/account")).build();
  }

  @GET
  @Produces(InetMediaType.IMAGE_PNG_VALUE)
  @Path("/{pushType}/icon")
  public Response getIcon(@PathParam("pushType") PushType pushType) throws Exception {

    Plugin plugin = PluginManager.getPlugin(pushType);
    byte[] bytes = plugin.getIcon();

    return Response.ok(bytes, InetMediaType.IMAGE_PNG_VALUE).build();
  }

  @Path("/account")
  public ManageAccountResource getManageAccountResource() {
    return new ManageAccountResource(context.getAccount());
  }

  @Path("/api-client/{clientName}")
  public ManageApiClientResource getManageApiClientResource(@PathParam("clientName") String clientName) throws Exception {
    return new ManageApiClientResource(clientName);
  }

  @POST
  @Path("/api-client")
  public Response newApiClient(@FormParam("clientName") String clientName, @FormParam("clientPassword") String clientPassword) throws Exception {

    if (context.getAccountStore().getByClientName(clientName) != null) {
      throw ApiException.badRequest(String.format("The client name %s already exists.", clientName));
    }

    CreateClientAction action = new CreateClientAction(clientName, clientPassword);

    ApiClient apiClient = context.getAccount().add(action);
    context.getAccountStore().update(context.getAccount());

    return Response.seeOther(new URI("manage/api-client/"+apiClient.getClientName())).build();
  }
}
