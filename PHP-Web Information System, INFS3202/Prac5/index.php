<html>
	<head>
	<title>INFS3202 P5</title>
	<meta name="description" content="search">
	<meta name="keywords" content="INFS3202 Prac5">
	<meta name="author" content="Martin Yu">
	<script src="js/jquery-1.10.2.js"></script>
	<script src="js/jquery-ui-1.10.4.custom.js"></script>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<script>
		$(function() {
				$( "#search" )
					.button()
			      	.click(function() {		        
						$("#result").empty();
						$.ajax({
							type: "POST",
							url: "query.php",
							data: {input:$("#input").val()},
							success: function (data) {
								var num = data.split("@").length-1;
								if(num == 0) {
									$("#result").append(
										"<strong>Sorry, no results matching your search were found</strong>"
									)
								}
								else {
									for(var i=0; i<num; i++) {
										var record = data.split("@")[i];
										if(i==0){
											$("#result").append(
												"<h3>Result:</h3>"
											)									
										}
										$("#result").append(
											"<strong>Item:</strong><br>Category:&nbsp"+record.split("  ")[0]+"<br>" 
											+record.split("  ")[1]+"&nbsp&nbsp&nbsp$"
											+record.split("  ")[2]+"&nbsp&nbsp&nbsp"
											+record.split("  ")[3]+"<br>" 
											+"<img class='img-thumbnail' style='width:200px; height:150px;' src='" +record.split("  ")[6]+"'>"+"<br>"
											+"Location:<br>"
											+record.split("  ")[4]+"<br><br>"
											+"Description:<br>"
											+record.split("  ")[5]+"<br><br>"
											+"Review:<br>"
											+record.split("  ")[7]+"<br><br><br>"
										)
									}
								}								
							}
						});
			    });
			});
	</script>
	</head>
	<body>
		<div>
			<h3>Search:</h3>
			<p><a href="comments.php">Comments</a></p>
			<div>
				<input id="input" type="text" class="form-control" placeholder="Text input">
			</div>
			<div style="margin-left:150px;">
				<button id="search" type="button" class="btn btn-success">Search</button>
			</div>

			<div id="result" style="float:left;">

			</div>		
		
		</div>
	</body>
</html>
