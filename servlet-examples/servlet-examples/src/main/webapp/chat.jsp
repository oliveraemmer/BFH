<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta charset="utf-8">
	<title>Chat JSP</title>
	<link rel="stylesheet" href="styles.css">
</head>
<body>
	<h1>Chat</h1>
	<form action="chat" method="post">
		<label>Your Message:</label>
		<input type="text" name="message" size="40" required>
		<input type="submit" value="Post">
	</form>
	<c:forEach items="${messages}" var="message">
		<p>${message.date}<br>${message.text}</p>
	</c:forEach>
</body>
</html>
