<?php
	$matches = array();
	$con = mysql_connect("localhost", "prac4", "") or die("Unable to connect to MySQL");
	$selected = mysql_select_db("P4", $con) or die("Could not select P4");
	$result = mysql_query("SELECT * FROM deals");
	if($result){
		while($row = mysql_fetch_array($result)){
			$json = array('id'=>$row['id'],'category'=>$row['category'],'name'=>$row['name'],
				'price'=>$row['price'],'dueTime'=>$row['dueTime'],'location'=>$row['location'],
				'description'=>$row['description'],'imageLink'=>$row['imageLink'],'review'=>$row['review'],
				'Latitude'=>$row['Latitude'],'Longitude'=>$row['Longitude']);

			array_push($matches, json_encode($json));

		}
	} else {
		die("There is no data");
	}
?>

<html>
	<head>
	<title>INFS3202 P6</title>
	<meta name="description" content="index">
	<meta name="keywords" content="INFS3202 Prac6">
	<meta name="author" content="Martin Yu">
	<link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet">
   	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui-1.10.4.custom.js"></script>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<style>
	#map{
	        height: 300px;
	        width: 300px;
	        padding: 0px;
	        clear: left;
	        margin-bottom: 30px;
	        float: left;
	      }  
	.mapdirection
	      {
	        height: 300px;
	        width: 300px;
	      }
    </style>
	<script>
		$(function() {
		    $( ".tabs" ).tabs({
		    		activate: function(event,ui) {

				       if ( ui.newPanel.selector=="#tab-3" ) {
				        	var id = $(this).attr("id");
				        	var dlat = $("#map-"+id+" .map-dlat").text();
				        	var dlon = $("#map-"+id+" .map-dlon").text();				        		        	
				        	adddirection(id,dlat,dlon); 	
				        }
				        
				    }
			    });
		    
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
	
	<script>
			function initialize() {
			  var mapOptions = {
			    zoom: 8,
			    center: new google.maps.LatLng(-27.62832, 152.75515)
			  };

			  var map = new google.maps.Map(document.getElementById('map'),
			      mapOptions);

				$( ".addmap" )
					.button()
			      	.click(function() {	
			      	var ID = $(this).attr("id");	
					$.ajax({
						type: "POST",
						url: "query.php",
						data: {func:'addmap',id:ID},
						success: function (data) {
							var latLng = new google.maps.LatLng(parseFloat(data.split("  ")[0]), parseFloat(data.split("  ")[1]));
				            var marker = new google.maps.Marker({
				                position: latLng,
				                map: new google.maps.Map(document.getElementById('map'),{center:latLng,zoom:15}),
				            });
						}
					});	        		
			    });			  
			}

			function loadScript() {
			  var script = document.createElement('script');
			  script.type = 'text/javascript';
			  script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&' +
			      'callback=initialize';
			  document.body.appendChild(script);
			}

		window.onload = loadScript;
    </script>
    
    
    <script>
    	var clat,clon;

    	function getLocation(){
			if (navigator.geolocation){
			    navigator.geolocation.getCurrentPosition(showPosition);
			} 
		}

		function showPosition(position){
			clat=position.coords.latitude;
			clon=position.coords.longitude;
		}

    	function adddirection(id,dlat,dlon){
    		var cPosition = new google.maps.LatLng(clat,clon);
    		var dPosition = new google.maps.LatLng(dlat,dlon);
    		
    		directionsDisplay = new google.maps.DirectionsRenderer(); 
            directionsService = new google.maps.DirectionsService();

    		var mapOptions = {
			    zoom: 16,
			    center: cPosition,
			    mapTypeId: google.maps.MapTypeId.ROADMAP
			};

		  	var map = new google.maps.Map(document.getElementById("map-"+id),
		    	mapOptions);

		  	directionsDisplay.setMap(map);

		  	var currentPositionMarker = new google.maps.Marker({
                    position: cPosition,
                    map: map,
                    title: "Current position"
            });

		  	calculateRoute(cPosition,dPosition);
    	}

    	function calculateRoute(cPosition,dPosition) {        
            if (cPosition != '' && dPosition != '') {

                var request = {
                    origin: cPosition, 
                    destination: dPosition,
                    travelMode: google.maps.DirectionsTravelMode["DRIVING"]
                };

                directionsService.route(request, function(response, status) {
                    if (status == google.maps.DirectionsStatus.OK) {
                        directionsDisplay.setDirections(response);
                    }
                });
            }  
        }
    </script>
    
	</head>
	<body onload="getLocation();">
	<h3>Comments Page</h3>
	<div style="width:70%;">
	<div id="map"></div>
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
					$Latitude = json_decode($record)->Latitude;
					$Longitude = json_decode($record)->Longitude;
					$eachcomment = explode(";",$review);
					$num = intval(count($eachcomment))-1;
					$i=0;

					echo "<div id='$id' style='float:left; clear:left; margin-left:60px;'>";
					echo "<br><br><br><strong>Item:</strong><br>Category:&nbsp$category<br>";
					echo "$name &nbsp&nbsp&nbsp $$price &nbsp&nbsp&nbsp $dueTime<br>";
					echo "<img src='$imageLink' class='img-thumbnail' style='width:200px; height:150px;'><br><br>";
					echo "<button  id='$id' class='addmap'>Location</button><br>";
					echo "<br>$location<br><br>";
					echo "<div id='$id' class='tabs' style='width:600px;'>
						<ul><li><a href='#tab-1'>description</a></li>
						<li><a href='#tab-2'>review</a></li>
						<li><a href='#tab-3'>direction</a></li></ul>
						<div id='tab-1'>$description</div>
						<div id='tab-3'><div id='map-$id' class='mapdirection'>
						<p class='map-dlat' style='display:none'>$Latitude</p>
						<p class='map-dlon' style='display:none'>$Longitude</p></div></div>
						<div id='tab-2'><ul id='$id' class='list-group result'>";
					for ($i=0; $i < $num; $i++) { 
						echo "<li class='list-group-item'>{$eachcomment[$i]}</li>";
					}
					echo "</ul><textarea class='form-control input' rows='3'></textarea><br>
						<div style='margin-left:310px;'>
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

