<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="fh5co-offcanvas">
  <a href="#" class="fh5co-close-offcanvas js-fh5co-close-offcanvas"><span><i class="icon-cross3"></i> <span>Close</span></span></a>
  <div class="fh5co-bio">
    <figure>
      <img src="${ownerImage}" alt="${appName}" class="img-responsive">
    </figure>
    <h3 class="heading">About Me</h3>
    <h2>${ownerName}</h2>
    <p>${ownerAbout}</p>
    <ul class="fh5co-social">
      <li><a href="${appFacebookUrl}"><i class="icon-facebook"></i></a></li>
    </ul>
  </div>

  <div class="fh5co-menu">
    <div class="fh5co-box">
      <h3 class="heading">Categories</h3>
      <ul>
        <c:forEach var="currTag" items="${navTags}">
          <c:url var="tagLink" value="/">
            <c:param name="tagId" value="${currTag.guid}" />
            <c:param name="tag" value="${currTag.name}" />
          </c:url>
          <li><a class="nav-link nav-item ${(not empty tag) and (currTag.name eq tag) ? '' : 'active'}" href="${tagLink}">${currTag.name}</a></li>
        </c:forEach>
      </ul>

    </div>
    <div class="fh5co-box">
      <h3 class="heading">Search</h3>
      <form method="GET" action="/">
        <div class="form-group">
          <input class="form-control" type="text" placeholder="Search" name="search" value="${search}">
          <button class="btn btn-success-outline" type="submit">Search</button>
        </div>
      </form>
    </div>
  </div>
</div>
<!-- END #fh5co-offcanvas -->
<header id="fh5co-header">

  <div class="container-fluid">

    <div class="row">
      <a href="#" class="js-fh5co-nav-toggle fh5co-nav-toggle"><i></i></a>
      <ul class="fh5co-social">
        <li><a href="${appFacebookUrl}"><i class="icon-facebook"></i></a></li>
      </ul>
      <div class="col-lg-12 col-md-12 text-center">
        <h1 id="fh5co-logo"><a href="/">&#x1F373; ${appName}</a></h1>
      </div>

    </div>

  </div>

</header>
<!-- END #fh5co-header -->