<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import="java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>INFS3202 P3</title>
	<meta name="description" content="Gallery of place">
	<meta name="keywords" content="INFS3202 Prac3">
	<meta name="author" content="Martin Yu">
	<link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet">
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui-1.10.4.custom.js"></script>
	<script type="text/javascript">
		function showform(n) {
			if(n==1) {
				if(document.getElementById("form1").style.display == "block")
				{
					document.getElementById("form1").style.display = "none";
				}
				else document.getElementById("form1").style.display = "block";
			}
			if(n==2) {
				if(document.getElementById("form2").style.display == "block")
				{
					document.getElementById("form2").style.display = "none";
				}
				else document.getElementById("form2").style.display = "block";
			}			
		}	
		
		function formValidation() {
			
			if(document.getElementById("name").value == "")
			{
				alert("All field cannot be empty!");
				return false;		
			}
			if(document.getElementById("price").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("price2").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("time").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("time2").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("location").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("location2").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("name2").value == "")
			{
				alert("All field cannot be empty!");
				return false;		
			}
			if(document.getElementById("imageURL").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("imageURL2").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("description").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("description2").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
			if(document.getElementById("location2").value == "")
			{
				alert("All field cannot be empty!");
				return false;
			}
		}
	</script>
	<script>
	$(function() {
		$( "#accordion" ).accordion({
	        heightStyle: "content"
	    });
		
		$( "#edit" ).button().click(function() {
	        $( "#dialog-form" ).dialog( "open" );
	    });
		
	});
	</script>
</head>
<%
	String name = "",price = "",time = "",location = "",imageURL = "",description = "";
	String line;
    int i = 1;
    try{
		BufferedReader reader = new BufferedReader(new FileReader(request.getRealPath("/")+"1.txt"));
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
<%
	String name2 = "",price2 = "",time2 = "",location2 = "",imageURL2 = "",description2 = "";
	String line2;
    int i2 = 1;
    try{
		BufferedReader reader2 = new BufferedReader(new FileReader(request.getRealPath("/")+"2.txt"));
		while ((line2 = reader2.readLine()) != null) {
			if(i2==1){
				 name2 = line2;
			} else if(i2==2) {
				 price2 = line2;
			} else if(i2==3) {
				 time2 = line2;
			} else if(i2==4) {
				 location2 = line2;
			} else if(i2==5) {
			 imageURL2 = line2;
			} else description2 = line2;
			i2++;		
		}
		reader2.close();
		}catch (Exception e) {//Catch exception if any
        System.err.println("Error: " + e.getMessage());
    }
%>

<body>
	<h3>Admin Page</h3>
	<div id="accordion" style="width:50%;">
	<h4>Mountain</h4>
	<div>Mountain tour <button id="edit" onclick="showform(1)">Edit</button>
	<form method="post" action="" onSubmit="return formValidation()">
		<table id="form1" style="display: none">
			<tr>
				<td>Name:</td>
				<td><input type="text" id="name" name="name" value="<%=request.getParameter("name")==null?name:request.getParameter("name")%>"></td>
			</tr>
			<tr>
				<td>Price:</td>
				<td><input type="text" id="price" name="price" value="<%=request.getParameter("price")==null?price:request.getParameter("price")%>"></td>
			</tr>
			<tr>
				<td>Due Time:</td>
				<td><input type="text" id="time" name="time" value="<%=request.getParameter("time")==null?time:request.getParameter("time")%>"></td>
			</tr>			
			<tr>
				<td>Location:</td>
				<td><input type="text" id="location" name="location" value="<%=request.getParameter("location")==null?location:request.getParameter("location")%>"></td>
			</tr>
			<tr>
				<td>Photo url:</td>
				<td><input type="text" id="imageURL"name="imageURL" value="<%=request.getParameter("imageURL")==null?imageURL:request.getParameter("imageURL")%>"></td>
			</tr>
			<tr>
				<td>Description:</td>
				<td><input type="text" id="description" name="description" value="<%=request.getParameter("description")==null?description:request.getParameter("description")%>"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Edit"></td>
			</tr>
		</table>
	</form>
	</div>
	<h4>Lake</h4>
	<div>Lake tour <button id="edit" onclick ="showform(2)">Edit</button>
	<form method="post" action="" onSubmit="return formValidation()">
		<table id="form2" style="display: none">
			<tr>
				<td>Name:</td>
				<td><input type="text" id="name2"  name="name2" value="<%=request.getParameter("name2")==null?name2:request.getParameter("name2")%>"></td>
			</tr>
			<tr>
				<td>Price:</td>
				<td><input type="text" id="price2" name="price2" value="<%=request.getParameter("price2")==null?price2:request.getParameter("price2")%>"></td>
			</tr>
			<tr>
				<td>Due Time:</td>
				<td><input type="text" id="time2" name="time2" value="<%=request.getParameter("time2")==null?time2:request.getParameter("time2")%>"></td>
			</tr>			
			<tr>
				<td>Location:</td>
				<td><input type="text" id="location2" name="location2" value="<%=request.getParameter("location2")==null?location2:request.getParameter("location2")%>"></td>
			</tr>
			<tr>
				<td>Photo url:</td>
				<td><input type="text" id="imageURL2" name="imageURL2" value="<%=request.getParameter("imageURL2")==null?imageURL2:request.getParameter("imageURL2")%>"></td>
			</tr>
			<tr>
				<td>Description:</td>
				<td><input type="text" id="description2" name="description2" value="<%=request.getParameter("description2")==null?description2:request.getParameter("description2")%>"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Edit"></td>
			</tr>
		</table>
	</form>
	</div>
</div>
	
</body>
</html>
<%
	if(request.getParameter("name") != null)
	{
		name=request.getParameter("name");
		price=request.getParameter("price");
		time=request.getParameter("time");
		location=request.getParameter("location");
		imageURL=request.getParameter("imageURL");
		description=request.getParameter("description");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(request.getRealPath("/")+"1.txt"));
		writer.write(name+"\n"+price+"\n"+time+"\n"+location+"\n"+imageURL+"\n"+description+"\n");
		writer.close();		
	}
%>

<%
	if(request.getParameter("name2") != null)
	{
		name2=request.getParameter("name2");
		price2=request.getParameter("price2");
		time2=request.getParameter("time2");
		location2=request.getParameter("location2");
		imageURL2=request.getParameter("imageURL2");
		description2=request.getParameter("description2");
		
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(request.getRealPath("/")+"2.txt"));
		writer2.write(name2+"\n"+price2+"\n"+time2+"\n"+location2+"\n"+imageURL2+"\n"+description2+"\n");
		writer2.close();		
	}
%>