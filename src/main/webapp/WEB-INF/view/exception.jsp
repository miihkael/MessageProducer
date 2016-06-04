<%--
  Created by IntelliJ IDEA.
  User: mika
  Date: 3.6.2016
  Time: 19:31
  To change this template use File | Settings | File Templates.
--%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Exception page</title>
</head>
<body>

<div class="container-fluid">
    <div class="container xd-container">

        <h2>Something happened...</h2>

        <p>${exception.message}</p>

        <!-- Exception: ${exception.message}.
		  	<c:forEach items="${exception.stackTrace}" var="stackTrace">
				${stackTrace}
			</c:forEach>
	  	-->

    </div>
</div>
</body>
</html>
