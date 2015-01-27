/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api.userevent;

import com.cosmicpush.app.resources.manage.client.userevents.*;
import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.*;
import java.util.List;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.exceptions.*;

public class UserEventDelegate extends AbstractDelegate {

  private final Account account;
  private final ApiClient apiClient;

  private final PluginContext context;
  private final UserEventPush userEvent;

  public UserEventDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, UserEventPush userEvent) {
    super(context.getObjectMapper(), apiRequest, context.getApiRequestStore());
    this.context = context;
    this.userEvent = ExceptionUtils.assertNotNull(userEvent, "userEvent");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.apiClient = ExceptionUtils.assertNotNull(apiClient, "apiClient");
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(userEvent);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return apiRequest.denyRequest(reasonNotPermitted);

    } else if (userEvent.isSendStory()) {
      return sendEmail(); // This is the "last" event so we can send the email.

    } if (userEvent.containsTrait("priority", "urgent")) {
      sendGoogleTalkIm(); // THIS is an "urgent" request so send the message now.
    }

    return apiRequest.processed();
  }

  private RequestStatus sendEmail() {
    String sessionId = userEvent.getSessionId();
    List<ApiRequest> requests = apiRequestStore.getByClientAndSession(apiClient, sessionId);

    String deviceId = null;
    for (ApiRequest request : requests) {
      if (request.getUserEventPush().getDeviceId() != null) {
        deviceId = request.getUserEventPush().getDeviceId();
        break;
      }
    }

    if (deviceId == null) {
      // We never found a device ID, we have an "invalid" request.
      return apiRequest.processed();
    }

    requests = apiRequestStore.getByClientAndDevice(apiClient, deviceId);
    UserEventGroup group = new UserEventGroup(requests);

    if (group.getBotName() != null || requests.size() == 1) {
      // not interested in sending an email.
      return apiRequest.processed();
    }

    UserEventSession session = group.findSession(userEvent);

    String userName = group.getUserName();
    if (StringUtils.isBlank(userName)) {
      userName = "Anonymous";
    }

    String story = String.format("<h1 style='margin:0'>New Story for %s</h1>\n", userName);

    story += "<fieldset style='margin-top:1em'>";
    story += "<legend>Request</legend><table>";
    story += tableRow("Bot", group.getBotName());
    story += tableRow("User Name", userName);
    story += tableRow("IP Address", session.getIpAddress());
    story += tableRow("Host Address", session.getHostAddress());
    story += tableRow("Session ID", session.getSessionId());
    story += "</table></fieldset>";

    UserAgent userAgent = session.getUserAgent();
    if (userAgent != null) {
      story += "<fieldset style='margin-top:1em'>";
      story += "<legend>User-Agent</legend><table>";
      story += tableRow(userAgent.getAgentType(), userAgent.getAgent());
      story += tableRow(userAgent.getOsType(), userAgent.getOs());
      story += "</table></fieldset>";
    }

    story += "<fieldset style='margin-top:1em'>";
    story += "<legend>Events</legend><table>";
    for (ApiRequest request : session.getApiRequests()) {
      String msg = request.getUserEventPush().getMessage();
      String time = Formats.defaultTime(request.getUserEventPush().getCreatedAt());
      story += tableRow(time, msg);
    }
    story += "</table></fieldset>";

    story += String.format("<p><a href='https://www.cosmicpush.com/manage/api-client/%s/user-events/%s'>More Information...</a></p>", apiClient.getClientName(), deviceId);

    story = String.format("<html>\n<body>\n%s</body>\n</html>\n", story);

    sendEmail(userName, story);

    return apiRequest.processed();
  }

  private String tableRow(String label, String value) {
    if (StringUtils.isBlank(value)) return "";

    return String.format("<tr><td style='white-space:nowrap'>%s:&nbsp;</td><td>%s</td></tr>\n",
        StringUtils.escapeHtml(label),
        StringUtils.escapeHtml(value));
  }

  private void sendEmail(String userName, String htmlContent) {

    if (PluginManager.getConfig(context, apiClient, SmtpEmailPush.PUSH_TYPE) != null) {
      sendSmtpEmail(userName, htmlContent);

    } else if (PluginManager.getConfig(context, apiClient, SesEmailPush.PUSH_TYPE) != null) {
      sendSesEmail(userName, htmlContent);

    } else {
      String msg = String.format("An email configuration was not specified.");
      throw ApiException.internalServerError(msg);
    }
  }

  private void sendSmtpEmail(String userName, String htmlContent) {
    try {
      SmtpEmailPush push = new SmtpEmailPush(
          "Jacob Parr <me@jacobparr.com>", "Munchie Monster Bot <bot@munchiemonster.com>",
          "New Story for " + userName, htmlContent, null
      );

      context.getPushProcessor().execute(account, apiClient, push);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void sendSesEmail(String userName, String htmlContent) {
    try {
      SesEmailPush push = new SesEmailPush(
          "Jacob Parr <me@jacobparr.com>", "Munchie Monster Bot <bot@munchiemonster.com>",
          "New Story for " + userName, htmlContent, null
      );

      context.getPushProcessor().execute(account, apiClient, push);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void sendGoogleTalkIm() {
    try {
      String id = apiRequest.getApiRequestId();
      String message = userEvent.getMessage() + " >> https://www.cosmicpush.com/q/" + id;
      GoogleTalkPush push = new GoogleTalkPush("jacob.parr@gmail.com", message, null);

      context.getPushProcessor().execute(account, apiClient, push);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
