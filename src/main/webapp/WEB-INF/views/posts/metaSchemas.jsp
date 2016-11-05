<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="keywords" value="" /> <c:forEach var="noteTagId" items="${noteTagIds}"> <c:forEach var="globalTag" items="${tags}"> <c:if test="${globalTag.guid eq noteTagId}"> <c:set var="keywords" value="${keywords} ${globalTag.name}" /> </c:if> </c:forEach> </c:forEach>
<title>${appName} - ${title}</title>
<meta name="description" content="Recipe for ${title}"/>
<meta name="keywords" content="${keywords}"/>
<%-- Facebook meta --%>
<meta property="og:title" content="${title}"/>
<meta property="og:type" content="recipe"/>
<meta property="og:description" content="Recipe for ${title}" />