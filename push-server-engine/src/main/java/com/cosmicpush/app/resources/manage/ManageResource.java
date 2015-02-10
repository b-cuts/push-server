/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage;

import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.resources.manage.account.ManageAccountResource;
import com.cosmicpush.app.resources.manage.client.ManageDomainResource;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.actions.CreateDomainAction;
import com.cosmicpush.common.clients.Domain;
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
  @Path("/{pushType}/icon-enabled")
  public Response getEnabledIcon(@PathParam("pushType") PushType pushType) throws Exception {

    Plugin plugin = PluginManager.getPlugin(pushType);
    byte[] bytes = plugin.getEnabledIcon();

    return Response.ok(bytes, InetMediaType.IMAGE_PNG_VALUE).build();
  }

  @GET
  @Produces(InetMediaType.IMAGE_PNG_VALUE)
  @Path("/{pushType}/icon-disabled")
  public Response getDisabledIcon(@PathParam("pushType") PushType pushType) throws Exception {

    Plugin plugin = PluginManager.getPlugin(pushType);
    byte[] bytes = plugin.getDisabledIcon();

    return Response.ok(bytes, InetMediaType.IMAGE_PNG_VALUE).build();
  }

  @Path("/account")
  public ManageAccountResource getManageAccountResource() {
    return new ManageAccountResource(context.getAccount());
  }

  @Path("/domain/{domainKey}")
  public ManageDomainResource getManageDomainResource(@PathParam("domainKey") String domainKey) throws Exception {
    return new ManageDomainResource(domainKey);
  }

  @POST
  @Path("/domain")
  public Response newDomain(@FormParam("domainKey") String domainKey, @FormParam("domainPassword") String domainPassword) throws Exception {

    if (context.getDomainStore().getByDomainKey(domainKey) != null) {
      throw ApiException.badRequest(String.format("The domain key %s already exists.", domainKey));
    }

    CreateDomainAction action = new CreateDomainAction(context.getAccount(), domainKey, domainPassword);

    Domain domain = context.getAccount().add(action);
    context.getDomainStore().create(domain);
    context.getAccountStore().update(context.getAccount());

    return Response.seeOther(new URI("manage/domain/"+domain.getDomainKey())).build();
  }
}
