<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-light">
  <a class="navbar-brand" href="/">&#x1F373; <spring:message code="app.name" /></a>
  <ul class="nav navbar-nav">
    <c:forEach var="currTag" items="${tags}">
      <li class="nav-item ${(not empty tag) and (currTag.name eq tag) ? 'active' : ''}">
        <c:url var="tagLink" value="/">
          <c:param name="tagId" value="${currTag.guid}" />
          <c:param name="tag" value="${currTag.name}" />
        </c:url>
        <a class="nav-link" href="${tagLink}">${currTag.name}</a>
      </li>
    </c:forEach>
  </ul>
  <form class="form-inline navbar-form pull-right" method="GET" action="/">
    <input class="form-control" type="text" placeholder="Search" name="search" value="${search}">
    <button class="btn btn-success-outline" type="submit">Search</button>
  </form>
</nav>