<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
--%>
<!DOCTYPE html>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.client.emails.EmailsModel"--%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f"   uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
  <head>
    <title>Requests</title>
    <%@include file="/_head-jquery.jsp"%>
  </head>
  <body>
    <div style="position: absolute; top: 0; right: 10px;">
      <h1>Cosmic Push</h1>
      <div>One API - Infinite Destinations</div>
    </div>

    <h1>${it.apiClient.clientName}: Requests</h1>
    <a href="<c:url value='/manage/account'/>">My Account</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}'/>">Manage</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/requests'/>">All Requests</a>&nbsp;&nbsp;
    Emails&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/notifications'/>">Notifications</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/user-events'/>">User Events</a>&nbsp;&nbsp;

    <table class="requests-table" style="width: 100%; margin-top: 1em">
      <colgroup>
        <col style="width:1em"/>
        <col style="width:1em"/>
        <col/>
        <col style="width:1em"/>
      </colgroup>
      <thead>
        <tr>
          <th></th>
          <th>Sent</th>
          <th colspan="2">Notes</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="apiRequest" items="${it.requests}">
          <tr>
            <td style="padding:2px; text-align: center">
              <c:choose>
                <c:when test="${apiRequest.requestStatus.pending}"><img src="<c:url value="/static/status-pending.png"/>" title="Processing is pending"/></c:when>
                <c:when test="${apiRequest.requestStatus.failed}"><img src="<c:url value="/static/status-failed.png"/>" title="Processing failed"/></c:when>
                <c:when test="${apiRequest.requestStatus.denied}"><img src="<c:url value="/static/status-denied.png"/>" title="Processing was denied"/></c:when>
                <c:when test="${apiRequest.requestStatus.warning}"><img src="<c:url value="/static/status-warning.png"/>" title="Processing partially failed"/></c:when>
                <c:otherwise><img src="<c:url value="/static/status-processed.png"/>" title="Processing has been completed"/></c:otherwise>
              </c:choose>
            </td>
            <td>
              <fmt:formatDate value="${apiRequest.createdAtUtilDate}" pattern="MM-dd-yy hh:mm a"/>
              <div>${apiRequest.remoteHost}</div>
            </td>
            <td style="border-right: 0">
              <div>To: <strong>${apiRequest.emailPush.toAddress}</strong></div>
              <div>Fr: <strong>${apiRequest.emailPush.fromAddress}</strong></div>
              <div><strong>${apiRequest.emailPush.emailSubject}</strong></div>
            </td>

            <td style="padding-right: 5px; border-left:0">
              <c:url var="url" value="/manage/api-client/${it.apiClient.clientName}/emails/${apiRequest.apiRequestId}"/>
              <a href="${url}" style="color: inherit">&gt;&gt;&gt;</a>
            </td>

          </tr>
        </c:forEach>
      </tbody>
    </table>

  </body>
</html>