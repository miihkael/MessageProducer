<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <link href="<c:url value="../resources/css/mainstyle.css" />" rel="stylesheet" />
</head>
<body>

<h2>Dummy Sensor Message Producer</h2>


<form method="POST" action="<c:url value='/home' />" >

    <th>Proceed as the following way:</th>
    <table>
        <tr>
            <td class="nbrcol01">1.)</td>
            <td class="txtcol01">Write 'sensor identification tag' in the 'Sensor Id Tag' field (this id can be any text
                and it will be included in every message sent by this dummy sensor).</td>
        </tr>
        <tr>
            <td class="nbrcol01">2.)</td>
            <td class="txtcol01">Choose below one of the message types to be sent:</td>
        </tr>
        <tr>
            <td></td>
            <th>
                <table>
                    <tr>
                        <td class="nbrcol01">a.)</td>
                        <td>For the integer type random number between -50 and 50 is sent repeatedly.</td>
                    </tr>
                    <tr>
                        <td class="nbrcol01">b.)</td>
                        <td>For the decimal type random number between 0.0 and 1.0 is sent repeatedly.</td>
                    </tr>
                    <tr>
                        <td class="nbrcol01">c.)</td>
                        <td>In case of the file based message sending, specify also an existing (data/text) file.
                            The Producer reads one line at a time from the file and sends it as a message. In the end of
                            the file the sending continues again at the beginning of the file.
                            NOTICE: This is server, so the file must be present on server side (in the server
                                    application directory (or it must be uploaded to the server)!
                        </td>
                    </tr>
                </table>
            </th>
        </tr>
        <tr>
            <td class="nbrcol01">3.)</td>
            <td class="txtcol01">'Submit' to start the message sending.</td>
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

    <table>
        <tr>
            <th class="nbrcol01">Sensor Id Tag:</th>
            <td><input id="sensidfield" type="text" name="strSensorId" value="${sessionData.strSensorId}" /></td>
        </tr>
        <tr>
            <th class="nbrcol01">Msg. type:</th>
            <th>
                <table>
                    <tr>
                        <td class="nbrcol01">Integer:</td>
                        <td>
                            <input class="radiobtn" type="radio" name="msgtype" value="Integeri" />
                            <!-- <input class="radiobtn" type="radio" name="msgtype" value="Integeri" /> -->
                        </td>
                    </tr>
                    <tr>
                        <td class="nbrcol01">Decimal:</td>
                        <td>
                            <input class="radiobtn" type="radio" name="msgtype" value="Decimali" />
                            <!-- <input class="radiobtn" type="radio" name="msgtype" value="Decimali" /> -->

                        </td>
                    </tr>
                    <tr>
                        <td class="nbrcol01">File:</td>
                        <td>
                            <input class="radiobtn" type="radio" name="msgtype" value="Filei"/>
                            <!-- <input class="radiobtn" type="radio" name="msgtype" value="Filei"/> -->
                        </td>
                    </tr>
                </table>
            </th>
        </tr>
        <tr>
            <th></th>
            <td>
                <input id="filefield" type="file" name="msgFile" value="${sessionData.msgFile}" size="50" />
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
                <button type="submit" name="actionbutton"><span>Start Messaging</span></button>
            </th>
        </tr>
    </table>
</form>

</body>
</html>
