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

<form action='<c:url value="/producing" />' method="POST">

    <h2>Sending messages .....:</h2>

    <table>
        <tr>
            <th class="nbrcol01">Sensor Id Tag:</th>
            <td>
                <input class="text" name="sensorid" value="${sessionData.strSensorId}" readonly />
            </td>
        </tr>
        <tr>
            <th class="nbrcol01">Message type:</th>
            <td>
                <input class="text" name="msgtyped" value="${sessionData.strMsgType}" readonly />
            </td>
        </tr>
        <tr>
            <th class="nbrcol01">Message File:</th>
            <td>
                <input class="text" name="msgfile" value="${sessionData.strMsgFile}" readonly />
            </td>
        </tr>
        <tr>
            <th></th>
            <th>
                <hr />
            </th>
        </tr>
        <tr>
            <th></th>
            <th>
                <input type="submit" name="actionbutton" value="Stop Messaging" id="idsubmit" />
            </th>
        </tr>
    </table>
</form>

</body>
</html>
