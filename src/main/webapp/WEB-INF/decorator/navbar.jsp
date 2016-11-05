<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-light">
  <a class="navbar-brand" href="/">&#x1F373; ${appName}</a>
  <div class="collapse navbar-toggleable-xs" id="collapsibleNav">
    <div class="nav navbar-nav">
      <c:forEach var="currTag" items="${navTags}">
        <c:url var="tagLink" value="/">
          <c:param name="tagId" value="${currTag.guid}" />
          <c:param name="tag" value="${currTag.name}" />
        </c:url>
        <a class="nav-link nav-item ${(not empty tag) and (currTag.name eq tag) ? '' : 'active'}" href="${tagLink}">${currTag.name}</a>
      </c:forEach>
    </div>
    <form class="form-inline navbar-form pull-xs-right" method="GET" action="/">
      <input class="form-control" type="text" placeholder="Search" name="search" value="${search}">
      <button class="btn btn-success-outline" type="submit">Search</button>
    </form>
  </div>
  <button class="navbar-toggler hidden-sm-up pull-xs-right" type="button" data-toggle="collapse" data-target="#collapsibleNav">
    &#9776;
  </button>
</nav>