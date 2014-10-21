<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>INFS3202 P3</title>
</head>
<%
	String name = "",price = "",time = "",location = "",imageURL = "",description = "";
	String line;
    int i = 1;
    try{
		BufferedReader reader = new BufferedReader(new FileReader(request.getRealPath("/")+"2.txt"));
		while ((line = reader.readLine()) != null) {
			if(i==1){
				 name = line;
			} else if(i==2) {
				 price = line;
			} else if(i==3) {
				 time = line;
			} else if(i==4) {
				 location = line;
			} else if(i==5) {
			 imageURL = line;
			} else description = line;
			i++;		
		}
		reader.close();
		}catch (Exception e) {//Catch exception if any
        System.err.println("Error: " + e.getMessage());
    }
%>

<body>
	<h2><%=name + "&nbsp&nbsp&nbsp" + price + "&nbsp&nbsp&nbsp" + time%></h2>
	<img src="<%=imageURL%>" width="299px" height="169px">
	<h2>Location: <%=location %></h2>
	<h3>Description:</h3>
	<p><%=description%></p>
</body>
</html>