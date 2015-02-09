package com.cosmicpush.app.resources.manage.client.twilio;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.resources.manage.client.ApiClientRequestsModel;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.TwilioPush;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageTwilioResource {

    private final Account account;
    private final ApiClient apiClient;
    private final ExecutionContext context = CpApplication.getExecutionContext();

    public ManageTwilioResource(Account account, ApiClient apiClient) {
        this.account = account;
        this.apiClient = apiClient;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Thymeleaf viewTwilios() throws Exception {

        List<ApiRequest> requests = new ArrayList<>();
        requests.addAll(context.getApiRequestStore().getByClientAndType(apiClient, TwilioPush.PUSH_TYPE));

        Collections.sort(requests);
        Collections.reverse(requests);

        ApiClientRequestsModel model = new ApiClientRequestsModel(account, apiClient, requests);
        return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_TWILIOS, model);
    }

    @GET
    @Path("/{apiRequestId}")
    @Produces(MediaType.TEXT_HTML)
    public Thymeleaf viewTwilio(@PathParam("apiRequestId") String apiRequestId) throws Exception {

        ApiRequest request = context.getApiRequestStore().getByApiRequestId(apiRequestId);
        TwilioPush twilioPush = request.getTwilioPush();

        ApiClientTwilioModel twilioModel = new ApiClientTwilioModel(account, apiClient, request, twilioPush);
        return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_TWILIO, twilioModel);
    }
}
