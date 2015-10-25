<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container">
  <div class="row">
    <c:if test="${not empty search}">
      <h3>Results for <em><c:out value="${search}" /></em>:</h3>
    </c:if>
    <c:if test="${not empty tag}">
      <h3>Recipes tagged as <em><c:out value="${tag}" /></em>:</h3>
    </c:if>
  </div>
  <div class="card-columns">
  <c:forEach var="note" items="${notes}">
    <c:set var="slug" value="${fn:toLowerCase(fn:replace(note.title, ' ', '-'))}" />
    <a class="card" href="/posts/${note.guid}/title/${slug}">
      <c:set var="noteImgUrl" value="${noteToImgUrlMap[note.guid]}" />
      <c:choose>
        <c:when test="${not empty noteImgUrl}">
          <img class="card-img-top img-responsive center-block" src="${noteImgUrl}" />
          <div class="card-footer small">${note.title}</div>
        </c:when>
        <c:otherwise>
          <div class="card-block small">${note.title}</div>
        </c:otherwise>
      </c:choose>
    </a>
  </c:forEach>
  </div>
  <c:import url="pagination.jsp" />
</div>