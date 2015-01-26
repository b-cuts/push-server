<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>
<%--@elvariable id="it" type="com.cosmicpush.app.resources.WelcomeModel"--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Cosmic Push</title>
    <%@include file="/_head-jquery.jsp"%>
  </head>
  <body>

    <div style="float: left">
      <h1>Cosmic Push</h1>
      <div>Your message - Infinite destinations</div>
    </div>

    <c:choose>
      <c:when test="${it.authenticated}">
        <div style="float: right">
          <a href="<c:url value='/manage/account'/>">My Account</a>
        </div>
      </c:when>
      <c:otherwise>
        <form name='f' action="<c:url value='/sign-in' />" method='POST' style="float: right">

          <table>
            <tr>
              <td><label for="user-name">User Name:</label></td>
              <td><input id="user-name" type='text' name='j_username' value=''></td>
            </tr>
            <tr>
              <td><label for="password">Password:</label></td>
              <td><input id="password" type='password' name='j_password' /></td>
            </tr>
            <tr>
              <td colspan='2'><button type="submit" style="width:100%">Sign In</button></td>
            </tr>
          </table>

        </form>
      </c:otherwise>
    </c:choose>

    <div style="clear:both"></div>

  </body>
</html>