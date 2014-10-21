<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Admin Log in</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<%
String uid = request.getParameter("userid");
String pwd = request.getParameter("password");
boolean valid = true;
if(uid!=null&&pwd!=null){
	if(uid.equals("admin")&&pwd.equals("password"))
	{
		//login succesfully
		session.setAttribute("uid", uid);
		response.sendRedirect("admin.jsp");
	}
	else
	{
		valid = false;
	}
}
%>
<body>
		<form method="POST" action=" ">
			<p>Login Information</p>
			Username:<input id="userid" type="text" name="userid" size="20"><br>
			Password:<input id="password" type="password" name="password" size="20"><br>
			<%if(!valid){ %>
          	<p><span class="errorMessage">User Id or Password is incorrect</span></p>
          	<%} %>
			
			<input type="submit" value="Login" name="login" size="20">
		</form>	
</body>
</html>