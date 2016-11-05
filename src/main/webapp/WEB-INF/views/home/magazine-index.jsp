<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container-fluid">
  <div class="row fh5co-post-entry">
    <c:if test="${not empty search}">
      <h3>Results for <em><c:out value="${search}" /></em>:</h3>
    </c:if>
    <c:if test="${not empty tag}">
      <h3>Recipes tagged as <em><c:out value="${tag}" /></em>:</h3>
    </c:if>
  </div>
  <div class="row fh5co-post-entry">
  <c:forEach var="note" items="${notes}" varStatus="loop">
    <c:set var="slug" value="${fn:toLowerCase(fn:replace(note.title, ' ', '-'))}" />
    <article class="col-lg-3 col-md-3 col-sm-3 col-xs-6 col-xxs-12 animate-box">
      <c:set var="noteImgUrl" value="${noteToImgUrlMap[note.guid]}" />
      <c:if test="${not empty noteImgUrl}">
        <figure>
          <a href="/posts/${note.guid}/title/${slug}">
            <img src="${noteImgUrl}" alt="Image" class="img-responsive">
          </a>
        </figure>
      </c:if>
      <span class="fh5co-meta">
        <c:forEach var="noteTagId" items="${note.tagGuids}">
          <c:forEach var="globalTag" items="${tags}">
            <c:if test="${globalTag.guid eq noteTagId}">
              <c:set var="noteTagName" value="${globalTag.name}" />
            </c:if>
          </c:forEach>
          <c:url var="tagLink" value="/">
            <c:param name="tagId" value="${noteTagId}" />
            <c:param name="tag" value="${noteTagName}" />
          </c:url>
          <a href="${tagLink}">${noteTagName}</a>
        </c:forEach>
      </span>
      <h2 class="fh5co-article-title"><a href="/posts/${note.guid}/title/${slug}">${note.title}</a></h2>

      <jsp:useBean id="created" class="java.util.Date"/>
      <jsp:setProperty name="created" property="time" value="${note.created}"/>
      <span class="fh5co-meta fh5co-date"><fmt:formatDate pattern="MMM dd, yyyy" value="${created}" /></span>
    </article>

    <c:choose>
      <c:when test="${loop.count % 4 == 0}">
			  <div class="clearfix visible-lg-block visible-md-block visible-sm-block visible-xs-block"></div>
      </c:when>
      <c:when test="${loop.count % 2 == 0}">
			  <div class="clearfix visible-xs-block"></div>
      </c:when>
    </c:choose>
  </c:forEach>
  </div>
  <c:import url="pagination.jsp" />
</div>