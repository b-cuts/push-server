<%@ page import="com.cosmicpush.pub.internal.EndPoints" %>
<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
--%>
<!DOCTYPE html>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.client.emails.EmailModel"--%>
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
      <div>Your message - Infinite destinations</div>
    </div>

    <h1>${it.apiClient.clientName}: Notification</h1>
    <a href="<c:url value='/manage/account'/>">My Account</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}'/>">${it.apiClient.clientName}</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/emails'/>">Emails</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/requests'/>">Requests</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/notifications'/>">Notifications</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/user-events'/>">User Events</a>&nbsp;&nbsp;

    <fieldset style="margin-top:1em">
      <legend>Details</legend>
      <c:url var="url" value="${it.retryUrl}"/>
      <form style="float: right" method="post" action="${url}" >
        <button type="submit" onclick="return confirm('Are you sure you want to retry sending this email?');">Resend</button>
      </form>
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

      <c:set var="path" value="notifications"/>
      <c:set var="compact" value="${false}"/>

    </fieldset>

    <fieldset style="margin-top:1em">
      <legend>Email</legend>
      <div>To: <strong>${it.request.emailPush.toAddress}</strong></div>
      <div>Fr: <strong>${it.request.emailPush.fromAddress}</strong></div>
      <div>&nbsp;</div>
      <div><strong>${it.request.emailPush.emailSubject}</strong></div>
      <div style="border: 2px solid black; padding: 0">${it.request.emailPush.htmlContent}</div>
    </fieldset>

    <fieldset style="margin-top:1em">
      <legend>Traits</legend>
      <c:set var="path" value="notifications"/>
      <c:set var="compact" value="${false}"/>
      <c:set var="traits" value="${it.email.traits}"/>
      <%@include file="_traits.jsp"%>
    </fieldset>

    <fieldset style="margin-top:1em">
      <legend>Notes</legend>
      <c:forEach var="note" items="${it.request.notes}">
        <div>${f:escapeXml(note)}</div>
      </c:forEach>
    </fieldset>

  </body>
</html>