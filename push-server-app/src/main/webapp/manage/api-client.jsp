<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.client.ManageApiClientModel"--%>
<html>
  <head>
    <title>Cosmic Push</title>
    <%@include file="/_head-jquery.jsp"%>
  </head>
  <body>
    <div style="position: absolute; top: 0; right: 10px;">
      <h1>Cosmic Push</h1>
      <div>Your message - Infinite destinations</div>
    </div>

    <h1>${it.apiClient.clientName}: Manage</h1>
    <a href="<c:url value='/manage/account'/>">My Account</a>&nbsp;&nbsp;
    Manage&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/requests'/>">All Requests</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/emails'/>">Emails</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/notifications'/>">Notifications</a>&nbsp;&nbsp;
    <a href="<c:url value='/manage/api-client/${it.apiClient.clientName}/user-events'/>">User Events</a>&nbsp;&nbsp;

    <c:if test="${it.message != null}">
      <div class="message">${it.message}</div>
    </c:if>

    <form style="margin-top: 1em" method="post">
      <fieldset style="margin-top: 1em">
        <legend>API Client</legend>
        <table style="width: 100%">
          <colgroup>
            <col style="width: 1em"/>
            <col style="width: 1em"/>
            <col/>
          </colgroup>
          <tbody>
            <tr>
              <td><label for="cp-client-name">Name:</label></td>
              <td><input type="text" id="cp-client-name" name="clientName" value="${it.apiClient.clientName}" style="width:200px;"></td>
              <td rowspan="3">&nbsp;</td>
            </tr>
            <tr>
              <td><label for="cp-client-password">Password:</label></td>
              <td><input type="text" id="cp-client-password" name="clientPassword" value="${it.apiClient.clientPassword}" style="width:200px;"></td>
            </tr>
            <tr>
              <td colspan="2" style="text-align: right">
                <c:url var="url" value='/manage/api-client/${it.apiClient.clientName}/client'/>
                <button type="submit" formaction="${url}" style="float:right">Update</button>
                <button type="submit" formaction="${url}/delete" style="float:left">Delete</button>
              </td>
            </tr>
          </tbody>
          </table>
      </fieldset>
    </form>

    <c:forEach var="plugin" items="${it.plugins}">
      ${plugin.htmlContent}
    </c:forEach>

  </body>
</html>

<%--
SMTP Username: AKIAJTE4W22D226AYCIQ
SMTP Password: AvqMJGZ1fcpwcrd1RcadFJFrWa4ttozNWeXZYHRqvPp+
--%>
