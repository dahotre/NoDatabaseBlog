<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <a class="card card-block" href="/posts/${note.guid}/title/${note.title}">
      <c:set var="noteImgUrl" value="${noteToImgUrlMap[note.guid]}" />
      <c:if test="${not empty noteImgUrl}">
        <img class="card-img-top img-responsive" src="${noteImgUrl}" />
      </c:if>
      <div class="card-title">${note.title}</div>
    </a>
  </c:forEach>
  </div>
  <c:import url="pagination.jsp" />
</div>