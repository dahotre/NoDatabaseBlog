<?xml version="1.0" encoding="UTF-8" ?>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %> <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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

  <%--
  <link rel="stylesheet" href="https://cdn.rawgit.com/twbs/bootstrap/v4-dev/dist/css/bootstrap.css">
  <link href='//fonts.googleapis.com/css?family=Merriweather:300,300italic,700|Merriweather+Sans:300,300italic'
      rel='stylesheet' type='text/css' >

  <link href="/resources/css/cook.css" rel="stylesheet" >
  --%>
  	<!-- Google Fonts -->
  	<link href='http://fonts.googleapis.com/css?family=Playfair+Display:400,700,400italic|Roboto:400,300,700' rel='stylesheet' type='text/css'>
  	<!-- Animate -->
  	<link rel="stylesheet" href="/resources/css/animate.css">
  	<!-- Icomoon -->
  	<link rel="stylesheet" href="/resources/css/icomoon.css">
  	<!-- Bootstrap  -->
  	<link rel="stylesheet" href="/resources/css/bootstrap.css">

  	<link rel="stylesheet" href="/resources/css/style.css">


  	<!-- Modernizr JS -->
  	<script src="/resources/js/modernizr-2.6.2.min.js"></script>
  	<!-- FOR IE9 below -->
  	<!--[if lt IE 9]>
  	<script src="/resources/js/respond.min.js"></script>
  	<![endif]-->

  <title><decorator:title default="${appName}"/></title>
  <decorator:head/>
</head>
<body itemscope itemtype="http://schema.org/WebPage">

  <div>
    <%@ include file="magazine-navbar.jsp" %>
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
  <footer id="fh5co-footer">
    <p><small>
        <%-- Get current date --%>
        <jsp:useBean id="date" class="java.util.Date" />
        &copy; ${appName}, <fmt:formatDate value="${date}" pattern="yyyy" />
    </small></p>
    <%-- JavaScript Libs And main application.js
    ================================================== --%>
    <!-- jQuery first, then Bootstrap JS. -->
    <%--
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://cdn.rawgit.com/twbs/bootstrap/v4-dev/dist/js/bootstrap.js"></script>
    <script src="/resources/js/app.js"></script>
    --%>


    <!-- jQuery -->
    <script src="/resources/js/jquery.min.js"></script>
    <!-- jQuery Easing -->
    <script src="/resources/js/jquery.easing.1.3.js"></script>
    <!-- Bootstrap -->
    <script src="/resources/js/bootstrap.min.js"></script>
    <!-- Waypoints -->
    <script src="/resources/js/jquery.waypoints.min.js"></script>
    <!-- Main JS -->
    <script src="/resources/js/main.js"></script>

    <decorator:getProperty property="page.arbitraryScript" />

  </footer>
</body>
</html>
