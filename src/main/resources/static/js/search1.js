//const fs = require('fs');

function myfun(){
	//alert("myfun");
	let list=[];
	var param = document.getElementsByTagName("p"); // review content
	var keyword = document.getElementsByTagName("h2")[0].innerHTML; // user input(query)

	document.getElementById('fileInput').addEventListener('change', function selectedFileChanged() {

		  const reader = new FileReader();
		  reader.onload = function fileReadCompleted() {
		    // 当读取完成时，内容只在`reader.result`中
		    stopwordFile=reader.result;
		    list= stopwordFile.split('\n')
		  };
		  reader.readAsText(this.files[0]);
		});
 
    // stop words list
	console.log(list)
	//list = ['a', 'an', 'and', 'of', 'or', 'if', 'able', 'above', 'according', 'accrodingly', 'across', 'actually']
//	let file = new File('data//stopword.txt');
//	let all = new FileReader().readAsText(file, 'utf-8');
//	let a = all.split("\n");
//	console.log(a);

//	const readFileLines = filename =>
//	  fs
//	    .readFileSync(filename)
//	    .toString('UTF8')
//	    .split('\n');
//	let list = readFileLines('data//stopword.txt');
//	console.log(list); 
	let keywordlist = keyword.split(" ");
	
	// 1. Filter all stop words out
	const keywordremoved = keywordlist.filter(function(word){
		return !list.includes(word);
	})
	console.log(keywordremoved);

	for(var i = 0; i < param.length - 1;i++){
		//console.log(param[i].innerHTML);
		//console.log(param[i].innerHTML.length);
		//console.log(param[i].innerHTML[0]);
		
		// 2. Get the review orginal content
		var words = param[i].innerHTML.split(" ");
        // 3. Loop every single word in the query
		for(var c = 0; c < keywordremoved.length; c++){
			var keywordLength = keywordremoved[c].length;
			    // 4. Loop every single word in review's original content
				for(var j = 0; j < words.length; j++){
					//console.log("word");
					//console.log(words[j]);
					
					// 5. If the content contains characters in the query 
					if (words[j].toLowerCase().includes(keywordremoved[c].toLowerCase())){
						// 6. Find the position of the character in this content 
						var index = words[j].indexOf(keywordremoved[c])
						console.log(index)
						// 7. Change its rendering style
						words[j] = "<b><font color='red'>"+ words[j].substring(index,keywordLength) +"</font></b>"+words[j].substring(keywordLength+1);
						
						//console.log("hello");	
					}
   
				}
		}
		//console.log("after add bold words")
		words = words.join(" ");
		console.log(words);
		
		// 8. Return the final highlighted content on the webpages
		param[i].innerHTML = words;
	} 
}/*用window.onload调用myfun()*/

window.onload = myfun;//不要括号
