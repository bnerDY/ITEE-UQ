<html>
	<head>
	<title>INFS3202 P4</title>
	<meta name="description" content="admin">
	<meta name="keywords" content="INFS3202 Prac4">
	<meta name="author" content="Martin Yu">
	<link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap.min.js"></script>
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui-1.10.4.custom.js"></script>
	<script>
	$(function() {
    var itemId = 0;
	var mode = "";
	var deleteID = 0;
      
    $( "#confirm-form" ).dialog({
		autoOpen: false,
		height:160,
		modal: true,
		buttons: {
			Yes: function() {        
			$.post("query.php", {func:"deletedata",id:deleteID},function(data){
			location.reload();
				}); 
			},
	        No: function() {
			$( this ).dialog( "close" );
			    }
		    }
	});
    //dialog form below.
	$( "#dialog-form" ).dialog({
		autoOpen: false,
		modal: true,
		buttons: {  		
		Update: function() {
		if($("#category").val()=="" || $("#name").val()==""){
		      $( ".validateTips" ).text( "category and name cannot be empty!" );
		} else {
			if(mode == "add"){
			$.post("query.php", {func:"adddata",category:$("#category").val(),name:$("#name").val(),
			price:$("#price").val(),dueTime:$("#dueTime").val(),location:$("#location").val(),
			description:$("#description").val(),imageLink:$("#imageLink").val(),review:$("#review").val()},function(data){
			location.reload();
			}); 
		} else {
			$.post("query.php", {func:"updatedata",id:itemID,category:$("#category").val(),name:$("#name").val(),
			price:$("#price").val(),dueTime:$("#dueTime").val(),location:$("#location").val(),
			description:$("#description").val(),imageLink:$("#imageLink").val(),review:$("#review").val()},function(data){
			location.reload();
				}); 
				}
			}
    	},
		Cancel: function() {
          	$( this ).dialog( "close" );
        		},
		    },
		});
    
	$( "#add" )
	.button()
	.click(function() {
		mode = "add";
		$("#category").val("");
		$("#name").val("");
		$("#price").val("");
		$("#dueTime").val("");
		$("#location").val("");
		$("#description").val("");
		$("#imageLink").val("");
		$("#review").val("");		        
		$("#dialog-form").dialog( "open" );
	 });
	 
	 $( ".edit" )
		.click(function() {		        
		itemID = $(this).attr("id");
		mode="edit";
		$.post("query.php", {func:"getdata",id:$(this).attr("id")},function(data){
		$("#category").val(data.split("  ")[0]);
		$("#name").val(data.split("  ")[1]);
		$("#price").val(data.split("  ")[2]);
		$("#dueTime").val(data.split("  ")[3]);
		$("#location").val(data.split("  ")[4]);
		$("#description").val(data.split("  ")[5]);
		$("#imageLink").val(data.split("  ")[6]);
		$("#review").val(data.split("  ")[7]);
		}); 
		$( "#dialog-form" ).dialog( "open" );
	 });
	 
	 $( ".remove" )
		.click(function() {		        
		deleteID = $(this).attr("id");
		$( "#confirm-form" ).dialog( "open" );
	 });
	 
	 
  	});
  	
	</script>
	</head>
	<body>
	<h2>Admin Page</h2>
	<div style="width:40%;">
	<table id="res" class="table table-striped table-bordered">
		<thead>
		    <tr>
		    	<th>Name</th>
		    </tr>
		</thead>
		<tbody>
		<?php
			$con = mysql_connect("localhost", "root", ""); 
			if (!$con) {
				die('Could not connect: '.mysql_error()); 
			}
			mysql_select_db("P4", $con);          
			$q = "SELECT * FROM deals";                            
			$res = mysql_query($q, $con); 
			$matches = array();               
			if($res){
				while($row = mysql_fetch_array($res)){
				array_push($matches, $row['id'].",".$row['name']);
				}
			} else {
				die("There is no data");
			}
			foreach($matches as $item) {
			$name = explode(",",$item)[1];
			$id = explode(",",$item)[0];		
			echo "<tr><td>$name</td><td><a id='$id' class='edit'>Edit</a></td><td><a id='$id' class='remove'>Remove</a></td></tr>";
			}
		?>           
		</tbody>
	</table>
	<p>
		<button id="add" type="button" style="float:left;">Create</button>
	</p>
	</div>

	<div id="dialog-form">
  	<form>
			<label for="category">Category</label>
			<input type="text" name="category" id="category" class="text ui-widget-content ui-corner-all">
			<label for="name">Name</label>
			<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all">
			<label for="price">Price</label>
			<input type="text" name="price" id="price" class="text ui-widget-content ui-corner-all">
			<label for="dueTime">Due Time</label>
			<input type="text" name="dueTime" id="dueTime" class="text ui-widget-content ui-corner-all">
			<label for="location">Location</label>
			<input type="text" name="location" id="location" class="text ui-widget-content ui-corner-all">
			<label for="description">Description</label>
			<input type="text" name="description" id="description" class="text ui-widget-content ui-corner-all">
			<label for="link">Image Link</label>
			<input type="text" name="imageLink" id="imageLink" class="text ui-widget-content ui-corner-all">
			<label for="review">Review(s)</label>
			<input type="text" name="review" id="review" class="text ui-widget-content ui-corner-all">
	</form>
	</div>
	
	<div id="confirm-form">	
	<form>
		<p>Are you sure?</p>			
	</form>
	</div>

	</body>
</html>