<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main page</title>
</head>
<body>

<p>
    Cars total in DB: ${itemsInDB}
</p>

<p>
    Tasks in progress:
    <c:forEach var="task" items="${tasksInProgress}" varStatus="counter">

<div>
        ${task}
</div>
</c:forEach>
</p>

</body>
</html>
