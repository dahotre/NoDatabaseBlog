<?xml version="1.0" encoding="UTF-8" ?>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<!--
  <link rel="shortcut icon" href="/favicon.ico">
  <link rel="icon" href="/icon.png">
  <link rel="apple-touch-icon" href="/touch-icon.png">
-->
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="stylesheet" href="https://cdn.rawgit.com/twbs/bootstrap/v4-dev/dist/css/bootstrap.css">
  <link href='//fonts.googleapis.com/css?family=Merriweather:300,300italic,700|Merriweather+Sans:300,300italic'
      rel='stylesheet' type='text/css' >

  <link href="/resources/css/app.css" rel="stylesheet" >

  <spring:message code="app.name" var="appName" />
  <title><decorator:title default="${appName}"/></title>
  <decorator:head/>
</head>
<body itemscope itemtype="http://schema.org/WebPage">

  <div>
    <%@ include file="navbar.jsp" %>
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
  <footer class="container">
    <div class="row">
      <div class="col-xs-12">
        &copy; ${appName}, 2015
      </div>
    </div>
    <%-- JavaScript Libs And main application.js
    ================================================== --%>
    <!-- jQuery first, then Bootstrap JS. -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://cdn.rawgit.com/twbs/bootstrap/v4-dev/dist/js/bootstrap.js"></script>
    <script src="/resources/js/app.js"></script>

    <decorator:getProperty property="page.arbitraryScript" />

  </footer>
</body>
</html>
