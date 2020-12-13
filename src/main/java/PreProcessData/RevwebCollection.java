package PreProcessData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Classes.Path;
import Classes.Review;

/**
 * Preprocess collection document by document.
 * Here, document is a single restaurant, which involves multiple reviews. 
 */
public class RevwebCollection implements DocumentCollection {
	
	private static final String HTML_TAG = "(<(.*?)>)|(\\[(.*?)\\])|(\\&\\#?\\S+( ))|(( )\\S+>( ))";
	private BufferedReader br = null;

	public RevwebCollection() throws IOException {
		FileInputStream fis = new FileInputStream(Path.CORPUS);
		InputStreamReader isr = new InputStreamReader(fis);
		br = new BufferedReader(isr);
	}
	
	/**
	 * Get a restaurant, which has multiple reviews.
	 */
	public List<Object> nextDocument() throws IOException {
		
		List<Object> reviewList = new ArrayList<>();
		String line = null;
		String url = null; 
	    String restID = null;
		String restName = null;
		String restDesc = null;
		String revID = null;
		String revContent = null;
		if((line = br.readLine()) != null) {
			while(!line.equals("<restaurant>")) {
				line = br.readLine();
			}
			
			if(line.equals("<restaurant>")) { 
				// Get url
				line = br.readLine();  
				url = line.substring(5, line.length()-6);
				
				// Get restaurant's ID 
				line = br.readLine();
				restID = line.substring(5, line.length()-6);
				
				// Get restaurant's name
				line = br.readLine();
				restName = line.substring(7, line.length()-8);
				
				// Get restaurant's description
				line = br.readLine();
				restDesc = line.substring(13, line.length()-14); 
			}
 
			while (!line.equals("<reviews>")) { 
				line = br.readLine();
			}
			
		    // Here, we reach the line containing tag "<reviews>"
			line = br.readLine();
			
			int i = 1; // Keep track of each review
			while(!line.equals("</reviews>")) {	
				// Intiate a review object in each interation 
				Review review = new Review();
				
				// Set its restaurant's url, name and desc
				review.setRestUrl(url);
				review.setRestName(restName); 
				review.setRestDesc(restDesc);
				
				// Get review's ID
				revID = restID + "-" + String.valueOf(i); 
				review.setId(revID);
				
				revContent = line.replaceAll(HTML_TAG, " ").replaceAll("( )+", " ").trim();
				review.setOriginalContent(revContent);
				
				reviewList.add(review);
				
				i++;
				line = br.readLine();
			}
			br.readLine();
			return reviewList;
		}
		
		if(br != null) {
			br.close();
		}
			
		return null;
	}
}
