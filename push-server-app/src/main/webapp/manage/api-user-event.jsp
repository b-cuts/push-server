<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>

<!DOCTYPE html>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.client.userevents.UserEventSessionsModel"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head>
    <title>Notifications</title>
    <%@include file="/_head-jquery.jsp"%>
    <script type="text/javascript">
      function showTraits(id) {
        $("#traits-"+id).css("display", "table");
        $("#show-"+id).css("display", "none");
        $("#hide-"+id).css("display", "inline");
        return false;
      }
      function hideTraits(id) {
        $("#traits-"+id).css("display", "none");
        $("#show-"+id).css("display", "inline");
        $("#hide-"+id).css("display", "none");
        return false;
      }
    </script>
    <style type="text/css">
      .traits-table {
        display: none;
      }
    </style>
  </head>
  <body>
    <div style="position: absolute; top: 0; right: 10px;">
      <h1>Cosmic Push</h1>
      <div>Your message - Infinite destinations</div>
    </div>

    <h1>${it.apiClient.clientName}: Notifications</h1>
    <a href="<c:url value='/manage/account'/>">My Account</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}'/>">${it.apiClient.clientName}</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/requests'/>">All Requests</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/emails'/>">Emails</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/notifications'/>">Notifications</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/user-events'/>">User Events</a>&nbsp;&nbsp;

    <table class="requests-table" style="width: 100%; margin-top: 1em">
      <thead>
        <tr>
          <th>Device ID: ${it.deviceId}</th>
        </tr>
      </thead>
    </table>

    <c:forEach var="session" items="${it.sessions}">
      <table class="requests-table" style="width: 100%; margin-top: 1em">

        <colgroup>
          <col style="width:1em"/>
          <col/>
          <col style="width:1em"/>
        </colgroup>

        <thead>
          <tr>
            <th colspan="3">
              <table>
                <tr>
                  <th><label>User Name:&nbsp;</label></th>
                  <th>${session.userName}</th>
                </tr>
                <tr>
                  <th><label>IP Address:&nbsp;</label></th>
                  <th>${session.ipAddress}</th>
                </tr>
                <c:if test="${session.ipAddress != session.hostAddress}">
                  <tr>
                    <th><label>Host Address:&nbsp;</label></th>
                    <th>${session.hostAddress}</th>
                  </tr>
                </c:if>
                <tr>
                  <th><label>Session ID:&nbsp;</label></th>
                  <th>${session.sessionId}</th>
                </tr>
                <c:if test="${session.userAgent != null}">
                  <tr>
                    <th><label>${session.userAgent.agentType}:&nbsp;</label></th>
                    <th>${session.userAgent.agentName} ${session.userAgent.agentVersion}</th>
                  </tr>
                  <tr>
                    <th>${session.userAgent.osType}:</th>
                    <th>${session.userAgent.osName} ${session.userAgent.osVersionNumber}</th>
                  </tr>
                </c:if>
              </table>
            </th>
          </tr>
        </thead>

        <tbody>
          <c:forEach var="apiRequest" items="${session.apiRequests}">
            <tr>
              <td><fmt:formatDate value="${apiRequest.userEventPush.createdAtUtilDate}" pattern="MM-dd-yy hh:mm a"/></td>
              <td style="border-right:0">
                <div>${f:escapeXml(apiRequest.userEventPush.message)}</div>

                <c:set var="path" value="user-events"/>
                <c:set var="compact" value="${false}"/>
                <c:set var="traits" value="${apiRequest.userEventPush.traits}"/>
                <%@include file="_traits.jsp"%>

              </td>
              <td style="padding-right: 5px; border-left:0">
                <c:if test="${f:length(apiRequest.userEventPush.traits) > 0}">
                  <a id="show-${apiRequest.key}" href="#" style="color: inherit; text-decoration: none" onclick="return showTraits('${apiRequest.key}');">&uarr;&uarr;&uarr;</a>
                  <a id="hide-${apiRequest.key}" href="#" style="color: inherit; text-decoration: none; display: none" onclick="return hideTraits('${apiRequest.key}');">&darr;&darr;&darr;</a>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </tbody>

      </table>
    </c:forEach>

  </body>
</html>