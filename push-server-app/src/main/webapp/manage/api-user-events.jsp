<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>

<!DOCTYPE html>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.client.userevents.UserEventGroupsModel"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head>
    <title>Notifications</title>
    <%@include file="/_head-jquery.jsp"%>
  </head>
  <body>
    <div style="position: absolute; top: 0; right: 10px;">
      <h1>Cosmic Push</h1>
      <div>Your message - Infinite destinations</div>
    </div>

    <h1>${it.apiClient.clientName}: Notifications</h1>
    <a href="<c:url value='/manage/account'/>">My Account</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}'/>">Manage</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/requests'/>">All Requests</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/emails'/>">Emails</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/notifications'/>">Notifications</a>&nbsp;&nbsp;
    User Events&nbsp;&nbsp;

    <table class="requests-table" style="width: 100%; margin-top: 1em">
      <colgroup>
        <col style="width:1em"/>
        <col style="width:1em"/>
        <col/>
        <col style="width:1em"/>
      </colgroup>

      <c:forEach var="userEventGroups" items="${it.collection}">
        <c:forEach var="userEventGroup" items="${userEventGroups}" varStatus="status">
          <c:set var="last" value="${status.last}"/>
          <c:if test="${status.first}">
            <thead>
              <tr>
                <th>Updated</th>
                <th>User Name</th>
                <th colspan="2">
                  <div style="float:left">Messages (last 3)</div>
                  <div style="float:right"><fmt:formatDate value="${userEventGroup.updatedAtUtilDate}" pattern="EEEE, MMMM d, yyYY"/></div>
                  <div style="clear: both;"></div>
                </th>
              </tr>
            </thead>
            <tbody>
          </c:if>
          <tr>
            <td><fmt:formatDate value="${userEventGroup.updatedAtUtilDate}" pattern="hh:mm a"/></td>
            <td>
              <c:choose>
                <c:when test="${userEventGroup.botName != null}">
                  <div>${userEventGroup.botName}</div>
                </c:when>
                <c:otherwise>
                  <div>${userEventGroup.userName}</div>
                  <div>${userEventGroup.ipAddress}</div>
                </c:otherwise>
              </c:choose>
            </td>
            <td style="border-right: 0; white-space: normal">
              <c:forEach var="userEvent" items="${userEventGroup.lastThree}" varStatus="status">
                <div>
                  <c:if test="${status.first && f:length(userEventGroup.lastThree) > 1}">
                    <span style="font-size:10px">${userEventGroup.count}/${f:length(userEventGroup.sessions)}...</span>
                  </c:if>
                  ${userEvent.userEventPush.message}
                </div>
              </c:forEach>
            </td>
            <td style="padding-right: 5px; border-left:0">
              <c:url var="url" value="/manage/api-client/${it.apiClient.clientName}/user-events/${userEventGroup.deviceId}"/>
              <a href="${url}" style="color: inherit">&gt;&gt;&gt;</a>
            </td>
          </tr>
          <c:if test="${last}">
             <tr><td style="border:0; background-color: transparent">&nbsp;</td></tr>
             </tbody>
           </c:if>
        </c:forEach>
      </c:forEach>
    </table>

  </body>
</html>