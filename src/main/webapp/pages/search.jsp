<%@ page language="java" 
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Search Results</title>
	
	<style type="text/css">
	    article {
	    	margin: auto;
	    	width: 800px;
	    }
	    
	    .title-section {
	        padding-top: auto 10px;
	    }
	    
	    .title-section img {
	    	margin-top: 30px;
	    	margin-bottom: 15px;
	    
	    }
	    
	    .title-section h3 {
	    	padding-left: 5px;
	    }
	    
	    .title-section h2 {
	    	padding-left: 5px;
	    }
	    
	    .result {
	    	border: 2px solid crimson; 
	    	border-radius: 5px;
	    	padding: 10px; 
	    	margin-top: 10px;
		    background-color: azure;
	    }
	    
	    .result p {
	        font-size: 18px;
	    }
	    
	    .result a {
	    	text-align: right;
	    	text-decoration: none;
	    }
	    
	    hr {
	    	width: 900px;
	    	margin-top: 20px;
	    }
	    
	    footer p {
	    	text-align: center;
	    	padding: 15px;
	    }
	</style>
</head>

<body>
    <article>
        <div class="title-section">
            <a href="/">
            	<img src="/images/Yelp_Logo.svg.png" alt="Yelp" 
                     height="100" width="300" />
            </a>
        	<h3>Top5 search results (${input[6].rcontent} seconds)</h3>
        	<h2>Query: ${input[5].rname}</h2>
        </div>
     	
 		<div class="result-section">
 		    <div class="result">
 		    	<p>${input[0].rcontent}</p>
				<a href=${input[0].rurl} target="_blank"><h4>${input[0].rname}</h4></a>
 		    </div>
 			
			<div class="result">
				<p>${input[1].rcontent}</p>
				<a href=${input[1].rurl} target="_blank"><h4>${input[1].rname}</h4></a>
			</div>
			
			<div class="result">
				<p>${input[2].rcontent}</p>
				<a href=${input[2].rurl} target="_blank"><h4>${input[2].rname}</h4></a>
			</div>
			
			<div class="result">
				<p>${input[3].rcontent}</p>	
				<a href=${input[3].rurl} target="_blank"><h4>${input[3].rname}</h4></a>
			</div>
			
			<div class="result">
				<p>${input[4].rcontent}</p>
				<a href=${input[4].rurl} target="_blank"><h4>${input[4].rname}</h4></a>
			</div>	
 		</div>
 		
    </article>

	<hr />
	<footer>
		<p>INFSCI 2140 Information Retrieval 2020 Fall</p>
	</footer>

	<script src="/js/search1.js"></script>
</body>

</html>