<?php
	$matches = array();
	$con = mysql_connect("localhost", "prac4", "") or die("Unable to connect to MySQL");
	$selected = mysql_select_db("P4", $con) or die("Could not select P4");
	$result = mysql_query("SELECT * FROM deals");
	if($result){
		while($row = mysql_fetch_array($result)){
			$json = array('id'=>$row['id'],'category'=>$row['category'],'name'=>$row['name'],
				'price'=>$row['price'],'dueTime'=>$row['dueTime'],'location'=>$row['location'],
				'description'=>$row['description'],'imageLink'=>$row['imageLink'],'review'=>$row['review']);
			array_push($matches, json_encode($json));

		}
	} else {
		die("There is no data");
	}
?>

<html>
	<head>
	<title>INFS3202 P5</title>
	<meta name="description" content="index">
	<meta name="keywords" content="INFS3202 Prac5">
	<meta name="author" content="Martin Yu">
	<link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet">
   	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui-1.10.4.custom.js"></script>
	<script>
		$(function() {
		    $( ".tabs" ).tabs();
			$( ".add" )
				.button()
			    .click(function() {	
			      	var ID = $(this).attr("id");
			      	if($("#"+ID+" .input").val()!=""){
					$.ajax({
						type: "POST",
						url: "query.php",
						data: {func:'addc',id:ID,input:$("#"+ID+" .coms").text()+' '+$("#"+ID+" .input").val()+';'},
						success: function (data) {
						$("#"+ID+".result").append(
							"<li class='list-group-item'>"+$("#"+ID+" .input").val()+"</li>"
						)
						$("#"+ID+" .input").val("");
						$("#"+ID+" .coms").text(data);
						}
					});		      			
			      }		      								
			    });
				$( ".cancel" )
					.button()
			      	.click(function() {		        
						$( ".input" ).val("");
			    });		    	
		});
	</script>
	</head>
	<body>
	<h3>Comments Page</h3>

	<div style="width:70%;">
	<table style = "margin-right=50px;" class="table table-striped table-bordered">
			<?php
				foreach ($matches as $record) {
					$id = json_decode($record)->id;
					$category = json_decode($record)->category;
					$name = json_decode($record)->name;
					$price = json_decode($record)->price;
					$dueTime = json_decode($record)->dueTime;
					$location = json_decode($record)->location;
					$description = json_decode($record)->description;
					$imageLink = json_decode($record)->imageLink;
					$review = json_decode($record)->review;
					$eachreview = explode(";",$review);
					$num = intval(count($eachreview))-1;
					$i=0;

					echo "<div id='$id' style='float:left; clear:left; margin-left:60px;'>";
					echo "<br><br><br><strong>Item:</strong><br>Category:&nbsp$category<br>";
					echo "$name &nbsp&nbsp&nbsp $$price &nbsp&nbsp&nbsp $dueTime<br>";
					echo "<img src='$imageLink' class='img-thumbnail' style='width:200px; height:150px;'><br><br>";
					echo "Location:<br>$location<br><br>";
					echo "<div class='tabs' style='width:300px;'>
						<ul><li><a href='#tab-1'>description</a></li>
						<li><a href='#tab-2'>review</a></li></ul>
						<div id='tab-1'>$description</div>
						<div id='tab-2'><ul id='$id' class='list-group result'>";
					for ($i=0; $i < $num; $i++) { 
						echo "<li class='list-group-item'>{$eachreview[$i]}</li>";
					}
					echo "</ul><textarea class='form-control input' rows='3'></textarea><br>
						<div>
						<button id='$id' class='add' type='button'>Add Comment</button>
						<button id='$id' class='cancel' type='button'>Cancel</button>
						<p class='coms' style='display:none'>$review</p>
						</div></div></div><br><br></div>";
				}
			?>
	</table>
	</div>
	</body>
</html>

