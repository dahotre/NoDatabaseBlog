<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
  <spring:message code="app.name" />
  <h1><c:out value="${title}" /></h1>
  <article>
    ${content}
  </article>
  <c:forEach var="tag" items="${tags}">
    <c:out value="${tag}" />
  </c:forEach>
</div>