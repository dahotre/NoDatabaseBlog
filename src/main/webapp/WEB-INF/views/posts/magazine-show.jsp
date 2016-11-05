<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>
  <%@include file="metaSchemas.jsp" %>
</head>
<div class="container-fluid">
  <div class="row fh5co-post-entry single-entry">
    <article class="col-lg-8 col-lg-offset-2 col-md-8 col-md-offset-2 col-sm-8 col-sm-offset-2 col-xs-12 col-xs-offset-0">
				<span class="fh5co-meta animate-box">
          <c:forEach var="noteTagId" items="${noteTagIds}">
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
				<h2 class="fh5co-article-title animate-box"><c:out value="${title}" /></h2>
				<span class="fh5co-meta fh5co-date animate-box"><fmt:formatDate pattern="MMM dd, yyyy" value="${created}" /></span>

				<div class="col-lg-12 col-lg-offset-0 col-md-12 col-md-offset-0 col-sm-12 col-sm-offset-0 col-xs-12 col-xs-offset-0 text-left content-article">
          ${content}
        </div>
    </article>
  </div>

  <div class="row fh5co-post-entry single-entry">
    <div id="disqus_thread" class="col-lg-12 col-lg-offset-0 col-md-12 col-md-offset-0 col-sm-12 col-sm-offset-0 col-xs-12 col-xs-offset-0 text-left"></div>
    <script type="text/javascript">
        /* * * CONFIGURATION VARIABLES * * */
        var disqus_shortname = 'juisrecipes';
        var disqus_identifier = '${guid}';

        /* * * DON'T EDIT BELOW THIS LINE * * */
        (function() {
            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
    <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript" rel="nofollow">comments powered by Disqus.</a></noscript>
  </div>
</div>