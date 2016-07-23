<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <link href="<c:url value="../resources/css/mainstyle.css" />" rel="stylesheet" />
</head>
<body>

<h2>Dummy Sensor Message Producer</h2>

<!--

-->
<form:form method="POST" action="/home" modelAttribute="sensorForm">
    <th class="txtcol01">Sensors are stored on 'SensorProducerSensorData.dat' file located at the
        application folder. To add, remove, or change sensor data, edit directly that file and finally
        reload sensor data by pressing 'Reload Sensors'.
    </th>
    <br/>
    <br/>
    <th>Proceed as the following way:</th>
    <table>
        <tr>
            <td class="nbrcol01">1.)</td>
            <td class="txtcol01">Mark the 'Sending' checkbox for the sensor to be messaging. Example of
                sensor types:</td>
        </tr>
        <tr>
            <td></td>
            <th>
                <table>
                    <tr>
                        <td class="nbrcol01">ACC: acceleration</td>
                        <td>Random number between -1.0 and 1.0: Three values for x, y, and z-axis separately.</td>
                    </tr>
                    <tr>
                        <td class="nbrcol01">HUM: humidity</td>
                        <td>Random number between 0 and 100 percent.</td>
                    </tr>
                    <tr>
                        <td class="nbrcol01">TMP: temperature</td>
                        <td>Random number between 15 and 25.
                        </td>
                    </tr>
                </table>
            </th>
        </tr>
        <tr>
            <td class="nbrcol01">2.)</td>
            <td class="txtcol01">Adjust the sending time-out (for the selected) sensors if necessary: Value can be
                between 300 ms and 60000 ms (default value is 500 ms).</td>
        </tr>
        <tr>
            <td class="nbrcol01">3.)</td>
            <td class="txtcol01">Press 'Start Messaging' to start the message sending.</td>
        </tr>
    </table>
    <hr />
    <br />

    <spring:hasBindErrors name="usererror">
        <c:forEach var="error" items="${errors.allErrors}">
            <!--  error is of type class FieldError -->
            <b>${error.field} <spring:message message="${error}" /></b>
            <br />
        </c:forEach>
        <hr />
        <br />
    </spring:hasBindErrors>

    <table id="buttons">
        <tr>
            <th id="buttonstart">
                <button type="submit" name="actionbutton" value="start"><span>Start Messaging</span></button>
            </th>
            <th id="buttonreload">
                <button type="submit" name="actionbutton" value="reload"><span>Reload Sensors</span></button>
            </th>
        </tr>
    </table>
    <br/>

    <table id="sensors">
        <thead>
            <tr>
                <th>No.</th>
                <th>SensorId/ApiKey</th>
                <th>Sensor Data Type</th>
                <th>Message File</th>
                <th>Timeout (ms)</th>
                <th>Sending</th>
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
                    <td>
                        <input type="number" name="sensorsInForm[${status.index}].timeOut" value="${sensor.timeOut}" min="300" max="60000"  />
                    </td>
                    <td>
                    <c:choose>
                        <c:when test="${sensor.selected==true}">
                            <input type="checkbox" name="sensorsInForm[${status.index}].selected" checked />
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="sensorsInForm[${status.index}].selected" />
                        </c:otherwise>
                    </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</form:form>

</body>
</html>
