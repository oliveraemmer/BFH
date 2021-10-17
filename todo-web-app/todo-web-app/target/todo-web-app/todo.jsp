<%--
  Created by IntelliJ IDEA.
  User: bfh
  Date: 15.10.2021
  Time: 22:29
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>todoList</title>
</head>
<body>
    <form action="todoServ" method="post">
        <label>Add ToDo Entry:</label><br>
        <label>Title: </label>
        <input type="text" name="title" required><br>
        <label>Category: </label>
        <input type="text" name="category" required><br>
        <label>Due Date: </label>
        <input type="date" name="dueDate" required><br>
        <input type="submit" value="entry">
    </form>
    <c:forEach items="${todoList}" var="todoList">
        <p>${todoList.title}<br>${todoList.category}<br>${todoList.dueDate}</p>
    </c:forEach>
</body>
</html>
