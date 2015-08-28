<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
  ${hello} from <spring:message code="app.name" />
  <div class="card-columns">
  <c:forEach var="note" items="${notes}">
    <div class="card card-block">
      <a class="card-title" href="/posts/${note.guid}/title/${note.title}">${note.title}</a>
      <p class="card-text">
        ${note}
      </p>
    </div>
  </c:forEach>
  </div>
  Total notes: ${noteCount}
</div>