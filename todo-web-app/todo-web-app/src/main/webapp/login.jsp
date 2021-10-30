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
    <form action="login" method="post">
        <label>Login</label><br>
        <label>User: </label>
        <input type="text" name="user" required><br>
        <label>Password: </label>
        <input type="password" name="password" required><br>
        <input type="submit" name="login" value="Login">
        <input type="submit" name="register" value="Register">
    </form>
</body>
</html>
