<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
  <%@include file="metaSchemas.jsp" %>
</head>
<div class="container">
  <h1><c:out value="${title}" /></h1>
  <article class="row">
    ${content}
  </article>
  <div class="row">
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
      <a class="label label-default" href="${tagLink}">${noteTagName}</a>
    </c:forEach>
  </div>
  <div class="row">
  <div id="disqus_thread"></div>
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