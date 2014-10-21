<?php
	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];
	session_start(); 
	if (($username == "infs") && ($password == "3202")){
		$_SESSION['username'] = "infs"; 
		$_SESSION['password'] = "3202";
		$_SESSION['timeout'] = time() + $_REQUEST['select'];
		$_SESSION['state'] = "login";
		header("Location: gallery.php");	
	} else {
		echo "Incorrect username/password";
	}
?>