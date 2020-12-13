package main;

import java.io.FileWriter;
import java.util.List;

import Classes.*;
import PreProcessData.*;

/**
 * Main entrance: Generate processed collection for following indexing.
 */
public class HW1Main {

	public static void main(String[] args) throws Exception {
		// Main entrance
		HW1Main hm1 = new HW1Main();
	
	    // Process 'revweb' collection
		long startTime = System.currentTimeMillis(); 
		hm1.PreProcess();                            
		long endTime = System.currentTimeMillis();  
		System.out.println("Web corpus running time: " + (endTime - startTime) / 60000.0 + " min");
	}

	/**
	 * Process collection (tokenization, normalization).
	 * 
	 * @param dataType 
	 * @throws Exception
	 */
	public void PreProcess() throws Exception {
		// Load the collection file and create the review collection object
		DocumentCollection corpus = new RevwebCollection(); 
	
		// Load stopword list and create a stopwordRemover and normalizer object
		StopWordRemover stopwordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();

		// Create a BufferedWriter to write result out into hard disk
		FileWriter wr = new FileWriter(Path.RESULT_CORPUS); 

		// A review list that holds all reviews under a restaurant
		List<Object> reviewList = null;

		// Process the corpus, restaurant by restaurant, iteractively
		int count = 0;
		while((reviewList = corpus.nextDocument()) != null) { // Get info of every restaurant
			// Iterate each review under the current restaurant
			for(Object review: reviewList) {
				// Obtain the current review object
				Review r = (Review) review;
				
				// Get its id -> 
				String revID = r.getId();
				// Write out it id
				wr.append(revID + "\n");
				
				// Get its restaurant's name -> 
				String restName = r.getRestName();
				// Get its restaurant's url ->
				String restUrl = r.getRestUrl();
				// Write out both name and url
				wr.append(restName + "\n");
				wr.append(restUrl + "\n");
				
				// Get its original content -> 
				String revOriginalContent = r.getOriginalContent();
				// Write out its original content
				wr.append(revOriginalContent + "\n");
				
				// Turn review's orginal content into char array for following preprocessing
				char[] revProprocessedContent = revOriginalContent.toCharArray();
				
				// Create a tokenizer object
				WordTokenizer tokenizer = new WordTokenizer(revProprocessedContent);
				
				// Create a word object, which can hold a word
				char[] word = null;
				
				// Process every word in the review
				while((word = tokenizer.nextWord()) != null) {
					// Each word is transformed into lowercase
					word = normalizer.lowercase(word);

					// Filter out stopwords, and only non-stopwords will be written into result file
					if(!stopwordRemover.isStopword(word))
						// Stemmed format of each word is written into result file
						wr.append(normalizer.stem(word) + " ");
				}
				
				// Finish processing one review, start a new line
				wr.append("\n"); 
				count++;	
			} // The end of inner for loop block
		} // The end of outer while loop block
		
		System.out.println("Total reivew count:  " + count); // 452
		wr.close();
	}
}
