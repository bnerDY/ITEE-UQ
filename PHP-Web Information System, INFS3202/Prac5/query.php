<?php
	$con = mysql_connect("localhost", "prac4", ""); 
	if (!$con) {
		die('Could not connect: '.mysql_error()); 
	}
	$db = mysql_select_db("P4", $con);
	$matches = array();
	
	if(isset($_POST['func']) && isset($_POST['id']) && isset($_POST['input']) && $_POST['func']=="addc"){
		mysql_query("UPDATE deals SET review = '{$_POST['input']}' WHERE id = '{$_POST['id']}' ");
		echo $_POST['input'];
	}
	if(isset($_POST['input'])){
		if(count(explode(" ",$_POST['input']))>1) {
			$input1 = explode(" ",$_POST['input'])[0];
			$input2 = explode(" ",$_POST['input'])[1];
			if (strpos($input2, '>') !== false) {
				$p = str_replace(">","",$input2);
				$result = mysql_query("SELECT * FROM deals WHERE (price > $p) AND (name LIKE '%{$input1}%' OR location LIKE '%{$input1}%')");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}

			} 
			if (strpos($input2, '<') !== false) {
				$p = str_replace("<","",$input2);
				$result = mysql_query("SELECT * FROM deals WHERE (price < $p)  AND (name LIKE '%{$input1}%' OR location LIKE '%{$input1}%')");
				if($result) {
					while($row = mysql_fetch_array($result)){
						echo $row['category']."  ".$row['name']."  ".$row['price']."  ".$row['dueTime']."  ".$row['location']."  ".$row['description']."  ".$row['imageLink']."  ".$row['review']."@";
					}				
				}
			} 
		}

		else {
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
	}
?>
