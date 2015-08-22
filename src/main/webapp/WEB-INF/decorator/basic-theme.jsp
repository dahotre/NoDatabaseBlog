<?xml version="1.0" encoding="UTF-8" ?>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="shortcut icon" href="favicon.ico">
  <link rel="icon" href="icon.png">
  <link rel="apple-touch-icon" href="touch-icon.png">

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet" >
  <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet" >
  <link href='//fonts.googleapis.com/css?family=Merriweather:300,300italic,700|Merriweather+Sans:300,300italic'
      rel='stylesheet' type='text/css' >

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <![endif]-->

  <link href="resources/css/app.css" rel="stylesheet" >

  <spring:message code="app.name" var="appName" />
  <title><decorator:title default="${appName}"/></title>
  <decorator:head/>
</head>
<body itemscope itemtype="http://schema.org/WebPage">

  <div>
    <c:if test="${param.debug == 'true' or (param.debug != 'false' and not empty cookie.debug) }">
      <style>
        .debug {
          display: block;
        }
      </style>
      <div class="alert alert-info alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <pre>
          <c:out value="Cookie: ${cookie}" />
          <c:out value="param: ${param}" />
          <c:out value="sessionScope: ${sessionScope}" />
          <c:out value="requestScope: ${requestScope}" />
        </pre>
      </div>
    </c:if>

    <c:if test="${not empty info}" >
      <div class="alert alert-success alert-dismissable center">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        ${info}
      </div>
    </c:if>
    <c:if test="${not empty error or not empty param.error}" >
      <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <c:out value="${not empty error ? error : param.error}" />
      </div>
    </c:if>

    <decorator:body />
  </div>

  <%-- A placeholder to display sections outside of main content --%>
  <decorator:getProperty property="page.outsideContainer" />

  <div class="footer navbar-fixed-bottom">
    <span class="pull-right stickyFeedback">
      <a class="typeform-share link" href="#" data-mode="2"
        target="_blank"><i class="fa fa-comment-o"></i><spring:message code="app.feedback" /></a>
    </span>
  </div>
  <footer class="container">
    <div class="row">
      <div class="col-xs-12">
        &copy; ${appName}, 2015
      </div>
    </div>
    <%-- JavaScript Libs And main application.js
    ================================================== --%>
    <script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
    <script src="//code.jquery.com/ui/2.1.1/jquery-ui.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <script src="resources/js/app.js"></script>

    <decorator:getProperty property="page.arbitraryScript" />

    <%-- Typeform script --%>
    <script>(function(){var qs,js,q,s,d=document,gi=d.getElementById,ce=d.createElement,gt=d.getElementsByTagName,id='typef_orm',b='https://s3-eu-west-1.amazonaws.com/share.typeform.com/';if(!gi.call(d,id)){js=ce.call(d,'script');js.id=id;js.src=b+'share.js';q=gt.call(d,'script')[0];q.parentNode.insertBefore(js,q)}})()</script>
  </footer>
</body>
</html>
