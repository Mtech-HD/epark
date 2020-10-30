<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ePark Registration</title>
</head>
<body>

	<center>
		<h1>Login Page</h1>
		
		<p style="color:red">${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
		
		<form action="register" method="post">
			<br />Username:<input type="text" name="username"> 
			<br />Password:<input type="password" name="password"> <br />
			<br />First Name:<input type="text" name="first_name"> 
			<br />Last Name:<input type="text" name="last_name"> 
			<br />Email:<input type="text" name="email"> 
			<br />User Type:<select id="usertype" name="usertype"> 
			<option value="User">User</option>option>
			<option value="CarParkOwner">CarParkOwner</option>
			<input type="submit" value="Submit">
		</form>
	
	<p>Already registered? <a href="/login">Login here</a></p>
	</center>

</body>
</html>