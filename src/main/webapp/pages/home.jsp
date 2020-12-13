<%@ page language="java" 
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Yelp Search Page</title>
	<style type="text/css">
		body {
			margin-top: 120px;
		}
		
		h1 {
			text-align: center;
		}

		img {
			display: block;
			margin: auto;
	    }
		
		#search-outer {
			text-align: center;
		}

	    #search-input {
			margin-left: auto;
			margin-right: auto;
			width: 600px;
			font-size: 20px;
			padding: 5px 0;
			border: 1px solid #2a9df4;
            border-radius: 3px;
	    }

		#search-submit {
			margin-left: 5px;
			margin-right: auto;
			font-size: 20px;
			padding: 5px 5px;
			border: 1px solid #EEEEEE;
            border-radius: 3px;
	    }
	    
	    article p {
	    	color: red;
	    	text-align: center;
	    	margin-right: 520px;
	    }
	</style>
</head>

<body>
	<article>
		<h1>Welcome Yelp Restaurant Search</h1>
    	<img src="/images/Yelp_Logo.svg.png" alt="Yelp" 
         	 height="100" width="300" /><br><br><br>
		<form action="search">
			<div id="search-outer">
			   	<input id="search-input" name="input" type="text"/>
				<input id="search-submit" type="submit" value="Search" />
			</div>
		</form>
		
		<p>${error}</p>
	</article>
	
</body>

</html>