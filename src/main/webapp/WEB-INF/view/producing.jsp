<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>

<%--
  Created by IntelliJ IDEA.
  User: mika
  Date: 2.6.2016
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>

<html>
<head>
    <link href="<c:url value="../resources/css/mainstyle.css" />" rel="stylesheet" />
    <title>Producing messages</title>
</head>
<body>

<form:form method="POST" action="/producing" modelAttribute="sensorForm">

    <spring:hasBindErrors name="usererror">
        <c:forEach var="error" items="${errors.allErrors}">
            <!--  error is of type class FieldError -->
            <b>${error.field} <spring:message message="${error}" /></b>
            <br />
        </c:forEach>
        <hr />
        <br />
    </spring:hasBindErrors>

    <br/>
    <br/>
    <th>
        <button type="submit" name="actionbutton" value="submit"><span>Stop Messaging</span></button>
    </th>

    <table id="sensors">
        <thead>
        <tr>
            <th>No.</th>
            <th>SensorId/ApiKey</th>
            <th>Sensor Data Type</th>
            <th>Message File</th>
            <th>Timeout (ms)</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <td><input type="hidden" name="sensorInForm"/></td>
        <c:forEach items="${sensorForm.sensorsInForm}" var="sensor" varStatus="status">
            <tr>
                <td aling="center">${status.count}</td>
                <td>${sensor.sensorId}<input type="hidden" name="sensorsInForm[${status.index}].sensorId" value="${sensor.sensorId}"/></td>
                <td>${sensor.sensorDataType}<input type="hidden" name="sensorsInForm[${status.index}].sensorType" value="${sensor.sensorDataType}"/></td>
                <td>${sensor.messageFile}<input type="hidden" name="sensorsInForm[${status.index}].messageFile" value="${sensor.messageFile}"/></td>
                <td>${sensor.timeOut}<input type="hidden" name="sensorsInForm[${status.index}].timeOut" value="${sensor.timeOut}"/></td>
                <td>${sensor.strStatus}<input type="hidden" name="sensorsInForm[${status.index}].strStatus" value="${sensor.strStatus}"/></td>
              </tr>
        </c:forEach>
        </tbody>
    </table>
</form:form>

</body>
</html>
