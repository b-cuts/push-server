<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>

<!DOCTYPE html>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.client.notifications.ApiClientNotificationModel"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head>
    <title>Notification</title>
    <%@include file="/_head-jquery.jsp"%>
  </head>
  <body>
    <div style="position: absolute; top: 0; right: 10px;">
      <h1>Cosmic Push</h1>
      <div>One API - Infinite Destinations</div>
    </div>

    <h1>${it.apiClient.clientName}: Notification</h1>
    <a href="<c:url value='/manage/account'/>">My Account</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}'/>">${it.apiClient.clientName}</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/emails'/>">Emails</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/requests'/>">Requests</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/notifications'/>">Notifications</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/user-events'/>">User Events</a>&nbsp;&nbsp;

    <fieldset>
      <table>
        <tr>
          <td><label>Status:</label></td>
          <td style="color: ${it.request.requestStatus.color}">${it.request.requestStatus.label}</td>
        </tr>
        <tr>
          <td><label>Sent:</label></td>
          <td><fmt:formatDate value="${it.request.createdAtUtilDate}" pattern="MM-dd-yy hh:mm a"/></td>
        </tr>
        <tr>
          <td><label>From:</label></td>
          <td>${it.request.remoteHost}</td>
        </tr>
      </table>

      <div style="margin: 1em 0; font-weight: bold">${f:escapeXml(it.notification.message)}</div>

    </fieldset>

    <fieldset>
      <legend>Traits</legend>
      <c:set var="path" value="notifications"/>
      <c:set var="compact" value="${false}"/>
      <c:set var="traits" value="${it.notification.traits}"/>
      <%@include file="_traits.jsp"%>
    </fieldset>

  </body>
</html>