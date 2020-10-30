<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ePark Login</title>
</head>
<body>



	<center>
		<h1>Login Page</h1>
		
		<p style="color:red">${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
		
		<form action="login" method="post">
			<br />Username:<input type="text" name="username"> <br />Password:<input
				type="password" name="password"> <br /> <input
				type="submit" value="Submit">
		</form>
		
				<p>New user? <a href="/registration">Register here</a></p>
	</center>


</body>
</html>