<html> 

  <head>
    <title>Gallery</title>
    <meta name="description" content="Gallery, bonus" />
    <meta id="keywords" content="INFS3202 prac1" />
	<meta id="author" content="Martin Yu" />
	<script type="text/javascript" src="scripts/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="scripts/lightbox-2.6.min.js"></script>
    <link type="text/css" rel="stylesheet" href="styles/style.css" />
    <link type="text/css" rel="stylesheet" href="styles/lightbox.css" />
    <script type="text/javascript">
		var leftTime;
		function counter(time, name)
		{	
			var timer = time;
			var hour = Math.floor(time/3600);
			time %= 3600;
			var min = Math.floor(time/60);
			time %= 60;
			var sec = time;
			sec--;
			//Counting down
			if (sec < 0) {	
				if ((min > 0) || (hour > 0)) {
					sec = 59;
					min--;
				}
			}
			//Counting down
			if (min < 0) {
				if (hour > 0) {
					min = 59;
					hour--;
				}
			}
			//MY.UQ like counting down interface
			if(name == "title")
			{
				if(timer>0)
				{
					var clocktext = "Time out " + hour + ":" + ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
					document.title = clocktext;	
					timer--;
					window.setTimeout("counter("+timer+",'" + name + "')", 1000);
				} else {
					document.title = "Time out " + "0:00:00";
				}
			} else {
				if(timer>0)
				{
					var clocktext = "Time out " + hour + ":" + ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
					document.getElementById(name).innerHTML  = clocktext;	
					timer--;
					window.setTimeout("counter("+timer+",'" + name + "')", 1000);
				} else {
					document.getElementById(name).innerHTML  = "Time out " + "0:00:00";	
				}	
			}
			if(timer == 0)
			{
				window.location.href = 'index.php';
			}
		}
		// Start counting
		function dealCounter()
		{
			if(document.getElementById("count").value == "Logout")
			{
				counter(leftTime,'c1');
			}
		}
		function getTime(time)
		{
			var endTime = new Date("2014/4/7,23:59:59");
			leftTime = endTime.getTime()/1000 - time;
			return leftTime;
		}
	</script>
	<?php 
		//Server side code.
		session_start(); 
		if(isset($_SESSION['username']) && isset($_SESSION['password'])){ 
			$file = fopen("../tmp/log.txt", "a");
			if ($_SESSION['state'] == "login")
			{
				fwrite($file, date("Y-m-d H:i:s", time()) ." ".$_SESSION['username'] ." Login"."\r\n");
			}	
			$_SESSION['state'] = "logout";
			$serverTime = time();
			$state = "Logout";
			$time = $_SESSION['timeout'] - time() ;	
			echo "<script type='text/javascript'>counter($time,'title');</script>";
			echo "<script type='text/javascript'>getTime($serverTime);</script>";
			if($_SESSION['timeout'] < time()) {	
				fwrite($file, date("Y-m-d H:i:s", time()) ." ".$_SESSION['username'] ." Logout" ." timer" ."\r\n");
				session_destroy();
				header("Location: index.php");	
			}
			fclose($file);
		} else {
			$state = "Login";
		}
	?>	
  </head>
  <body onload="dealCounter()">
  	<div id="login">
		<input type="button" id="count" value="<?php echo $state;?>" onclick="location = 'index.php' ">
	</div>
	<div>
		<h2>Places:</h2>
		<div id="c1"></div>
    	<ul>
  			<a href="images/image1.jpg" rel="lightbox">
  			<img src="images/image1.jpg" width="125" height="75" alt="description image 1" /></a>
			<a href="images/image2.jpg" rel="lightbox">
			<img src="images/image2.jpg" width="125" height="75" alt="description image 2" /></a>
			<a href="images/image3.jpg" rel="lightbox">
			<img src="images/image3.jpg" width="125" height="75" alt="description image 3" /></a>
			<a href="images/image4.jpg" rel="lightbox">
			<img src="images/image4.jpg" width="125" height="75" alt="description image 4" /></a>
		</ul>
	</div>
	<div>
		<h2>Places:</h2>
		<div></div>
		<ul>
			<a href="images/image5.jpg" rel="lightbox">
			<img src="images/image5.jpg" width="125" height="75" alt="description image 5" /></a>
			<a href="images/image6.jpg" rel="lightbox">
			<img src="images/image6.jpg" width="125" height="75" alt="description image 6" /></a>
			<a href="images/image7.jpg" rel="lightbox">
			<img src="images/image7.jpg" width="125" height="75" alt="description image 7" /></a>
			<a href="images/image8.jpg" rel="lightbox">
			<img src="images/image8.jpg" width="125" height="75" alt="description image 8" /></a>
  		</ul>
  	</div>
	
</body>
</html>