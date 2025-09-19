<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <!-- dùng contextPath + cache-busting -->
    <link rel="stylesheet" href="styles/main.css" type="text/css">
</head>
<body>

<c:if test="${empty sqlStatement}">
    <c:set var="sqlStatement" value="SELECT * FROM User"/>
</c:if>

<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the <b>Execute</b> button.</p>

<p><b>SQL statement:</b></p>
<form action="${pageContext.request.contextPath}/sqlGateway" method="post">
    <textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea>
    <br>
    <input type="submit" value="Execute">
</form>

<p><b>SQL result:</b></p>
${sqlResult}

</body>
</html>
