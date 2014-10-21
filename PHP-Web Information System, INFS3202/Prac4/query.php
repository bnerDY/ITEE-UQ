<?php
	$con = mysql_connect("localhost", "root", ""); 
	if (!$con) {
		die('Could not connect: '.mysql_error()); 
	}
	$db = mysql_select_db("P4", $con);
	$matches = array();
	
	if(isset($_POST['func']) && isset($_POST['category']) 
		&& isset($_POST['name']) && isset($_POST['price']) && isset($_POST['dueTime']) 
		&& isset($_POST['location']) && isset($_POST['description']) && isset($_POST['imageLink']) 
		&& isset($_POST['review']) && $_POST['func']=="adddata"){

		mysql_query("INSERT INTO deals (id, category, name, price, dueTime, location, 
			description, imageLink, review) VALUES (NULL,'{$_POST['category']}','{$_POST['name']}',
			'{$_POST['price']}','{$_POST['dueTime']}','{$_POST['location']}','{$_POST['description']}', 
			'{$_POST['imageLink']}','{$_POST['review']}') ");
	}
	
	if(isset($_POST['func']) && isset($_POST['id']) && $_POST['func']=="getdata"){
		$result = mysql_query("SELECT * FROM deals WHERE id = '{$_POST['id']}'");
		if($result){
			while($row = mysql_fetch_array($result)){
				echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review'];
			}
		}
	}
	
	if(isset($_POST['func']) && isset($_POST['id']) && isset($_POST['category']) 
		&& isset($_POST['name']) && isset($_POST['price']) && isset($_POST['dueTime']) 
		&& isset($_POST['location']) && isset($_POST['description']) && isset($_POST['imageLink']) 
		&& isset($_POST['review']) && $_POST['func']=="updatedata"){

		mysql_query("UPDATE deals SET category = '{$_POST['category']}', name = '{$_POST['name']}',price = '{$_POST['price']}',
			dueTime = '{$_POST['dueTime']}', location = '{$_POST['location']}', description = '{$_POST['description']}', imageLink = '{$_POST['imageLink']}',
			review = '{$_POST['review']}' WHERE id = '{$_POST['id']}'");
	}

	if(isset($_POST['func']) && isset($_POST['id']) && $_POST['func']=="deletedata"){
		mysql_query("DELETE FROM deals WHERE id = '{$_POST['id']}'");
	}
	
	if(isset($_POST['input'])){
		if (strpos($_POST['input'], '>') !== false) {
			$p = str_replace(">","",$_POST['input']);
			$result = mysql_query("SELECT * FROM deals WHERE price > $p");
			if($result) {
				while($row = mysql_fetch_array($result)){
					echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
				}				
			}
		} 
		if (strpos($_POST['input'], '<') !== false) {
			$p = str_replace("<","",$_POST['input']);
			$result = mysql_query("SELECT * FROM deals WHERE price < $p");
			if($result) {
				while($row = mysql_fetch_array($result)){
					echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
				}				
			}
		} 
		else {
			$result = mysql_query("SELECT * FROM deals WHERE price = '{$_POST['input']}' OR name LIKE '%{$_POST['input']}%' OR location LIKE '%{$_POST['input']}%'");
			if($result) {
				while($row = mysql_fetch_array($result)){
					echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
				}				
			}			
		}
	}
?>