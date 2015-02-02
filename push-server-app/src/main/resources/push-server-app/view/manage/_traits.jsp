<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  ~ Copyright (c) 2014 Jacob D. Parr
  ~
  ~ This software may not be used without permission.
  --%>

<table class="traits-table" id="traits-${apiRequest.apiRequestId}">
  <colgroup>
    <col style="width: 1em"/>
    <col/>
  </colgroup>
  <tbody>
    <c:forEach var="trait" items="${traits}">
      <tr>
        <td class="key">${trait.key}:&nbsp;</td>
        <c:choose>

          <c:when test="${f:toLowerCase(trait.key) == 'link'}">
            <td class="value"><a href="${trait.value}" target="_blank">${trait.value}</a></td>
          </c:when>

          <c:when test="${compact && f:toLowerCase(trait.key) == 'exception'}">
            <td class="value">
              <c:url var="url" value="/manage/api-client/${it.apiClient.clientName}/${path}/${apiRequest.apiRequestId}"/>
              <a href="${url}" style="color: inherit">view...</a>
            </td>
          </c:when>
          <c:when test="${!compact && f:toLowerCase(trait.key) == 'exception'}">
            <td class="value-data">${trait.value}</td>
          </c:when>

          <c:when test="${compact && f:toLowerCase(trait.key) == 'json'}">
            <td class="value">
              <c:url var="url" value="/manage/api-client/${it.apiClient.clientName}/${path}/${apiRequest.apiRequestId}"/>
              <a href="${url}" style="color: inherit">view...</a>
            </td>
          </c:when>
          <c:when test="${!compact && f:toLowerCase(trait.key) == 'json'}">
            <td class="value-data">${f:escapeXml(trait.value)}</td>
          </c:when>

          <c:otherwise>
            <td class="value">${f:escapeXml(trait.value)}</td>
          </c:otherwise>

        </c:choose>
      </tr>
    </c:forEach>
  </tbody>
</table>
