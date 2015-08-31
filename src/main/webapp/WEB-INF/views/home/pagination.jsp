<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${totalPages gt 1}">
  <nav>
    <ul class="pager">
      <c:if test="${page gt 1}">
        <c:url value="/" var="previousPageLink">
          <c:param name="page" value="${page - 1}" />
          <c:if test="${not empty search}">
            <c:param name="search" value="search" />
          </c:if>
        </c:url>
        <li><a href="${previousPageLink}">Previous</a></li>
      </c:if>
      <c:if test="${page lt totalPages}">
        <c:url value="/" var="nextPageLink">
          <c:param name="page" value="${page + 1}" />
          <c:if test="${not empty search}">
            <c:param name="search" value="search" />
          </c:if>
        </c:url>
        <li><a href="${nextPageLink}">Next</a></li>
      </c:if>
      <li>Total recipes: ${noteCount}</li>
    </ul>
  </nav>
</c:if>