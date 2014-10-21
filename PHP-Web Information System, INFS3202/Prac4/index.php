<html>
	<head>
	<title>INFS3202 P4</title>
	<meta name="description" content="index">
	<meta name="keywords" content="INFS3202 Prac4">
	<meta name="author" content="Martin Yu">
	<link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap.min.js"></script>
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui-1.10.4.custom.js"></script>
	</head>
	<body>
	<h2>Index Page</h2>
	<p>
		<a href = "admin.php"> Admin</a>
		<a href = "search.php"> Search</a>
	</p>
	<div style="width:70%;">
	<table style = "margin-right=50px;" class="table table-striped table-bordered">
		<?php
		$con = mysql_connect("localhost", "root", ""); 
		if (!$con) {
			die('Could not connect: '.mysql_error()); 
		}
		mysql_select_db("P4", $con);          
		$q = "SELECT * FROM deals";                            
		$res = mysql_query($q, $con);                
		if(!$res){
			die("Valid result!");
		}
		
		while($row = mysql_fetch_row($res)) 
		echo "<p>$row[1]</p><p>$row[2]</p><p=250>$row[3]</p>
		<p>$row[4]</p><p>$row[5]</p><p>$row[6]</p><p><img width='299px' height='169px' src='$row[7]'/></p><p>$row[8]</p>"; 
		mysql_free_result($res);
		mysql_close();                
		?> 
	</table>
	</div>
	</body>
</html>


