<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.manage.account.ManageAccountModel"--%>
<html>
  <head>
    <title>Cosmic Push - Manage Account</title>
    <%@include file="/_head-jquery.jsp"%>
  </head>
  <body>
    <div style="position: absolute; top: 0; right: 10px;">
      <h1>Cosmic Push</h1>
      <div>Your message - Infinite destinations</div>
    </div>

    <h1>My Account</h1>
    <a href="<c:url value='/'/>">Home Page</a>

    <fieldset style="margin-top: 1em">
      <legend>API Clients</legend>
      <c:forEach var="apiClient" items="${it.account.apiClients}">
        <div>
          <span style="color: #FFD700; font-size:22px;">${apiClient.clientName}</span>

          <c:forEach var="pushType" items="${it.pushType}">
            <img src="<c:url value="/manage/${pushType}/icon"/>" class="icon" style="margin-left: 5px"/>
          </c:forEach>

          <div style="float: right">
            <a style="margin-left: 10px" href="<c:url value='/manage/api-client/${apiClient.clientName}'/>">Manage</a>
            <a style="margin-left: 10px" href="<c:url value='/manage/api-client/${apiClient.clientName}/requests'/>">All Requests</a>
            <a style="margin-left: 10px" href="<c:url value='/manage/api-client/${apiClient.clientName}/emails'/>">Emails</a>
            <a style="margin-left: 10px" href="<c:url value='/manage/api-client/${apiClient.clientName}/notifications'/>">Notifications</a>
            <a style="margin-left: 10px" href="<c:url value='/manage/api-client/${apiClient.clientName}/user-events'/>">User Events</a>
          </div>
          <div style="clear:both"></div>

        </div>
      </c:forEach>

      <form method="post">
        <table style="margin-top: 1em">
          <colgroup>
            <col style="width: 1em"/>
            <col/>
          </colgroup>
          <tbody>
            <tr>
              <td><label for="client-name">Name:</label></td>
              <td><input type="text" id="client-name" name="clientName" style="width:200px;"></td>
            </tr>
            <tr>
              <td><label for="client-password">Password:</label></td>
              <td><input type="text" id="client-password" name="clientPassword" style="width:200px;"></td>
            </tr>
            <tr>
              <td colspan="2">
                <button style="float :right" type="submit" formaction="<c:url value='/manage/api-client'/>">Create</button>
              </td>
            </tr>
          </tbody>
        </table>
      </form>

    </fieldset>

    <fieldset style="margin-top: 1em">
      <legend>User Details</legend>
      <form method="post">
        <table>
          <colgroup>
            <col style="width: 1em"/>
            <col/>
          </colgroup>
          <tbody>
            <tr>
              <td><label>User Name:</label></td>
              <td class="data">${it.account.userName}</td>
            </tr>
            <tr>
              <td><label for="first-name">First Name:</label></td>
              <td><input type="text" id="first-name" name="firstName" value="${it.account.firstName}" style="width:200px;"></td>
            </tr>
            <tr>
              <td><label for="last-name">Last Name:</label></td>
              <td><input type="text" id="last-name" name="lastName" value="${it.account.lastName}" style="width:200px;"></td>
            </tr>
            <tr>
              <td><label for="email">Email:</label></td>
              <td><input type="text" id="email" name="emailAddress" value="${it.account.emailAddress}" style="width:200px;"></td>
            </tr>
            <tr>
              <td colspan="2">
                <button style="float: right" type="submit" formaction="<c:url value='/manage/account'/>">Update</button>
              </td>
            </tr>
          </tbody>
        </table>
      </form>
    </fieldset>

  </body>
</html>