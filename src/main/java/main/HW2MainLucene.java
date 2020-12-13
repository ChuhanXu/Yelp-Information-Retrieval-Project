package main;

import Classes.Review;
import IndexingLucene.*;

/**
 * Main entrance: Generate index for a collection.
 */
public class HW2MainLucene {

	public static void main(String[] args) throws Exception {
		// Main entrance
		HW2MainLucene hm2 = new HW2MainLucene();
		
		// Write index
		long startTime =  System.currentTimeMillis();
		hm2.writeIndex();
		long endTime = System.currentTimeMillis();
		System.out.println("Index web corpus running time: " + (endTime - startTime) / 60000.0 + " min"); 
	}

	/**
	 * Build index file.
	 * 
	 * @param dataType
	 * @throws Exception
	 */
	public void writeIndex() throws Exception {
		// Create preprocessed collection
		PreProcessedCorpusReader corpus = new PreProcessedCorpusReader();
		
		// Create the index writer object
		MyIndexWriter myiwriter = new MyIndexWriter();
		
		// Create a review object
		Object review = null;

		int count = 0;
		// Build index for corpus review by review
		while ((review = corpus.nextDocument()) != null) { 
			
			// Index the current review
		    myiwriter.index(review);
			
			count++;
			if(count%10000==0)
				System.out.println("Finish " + count + " docs.");
		}
		
		System.out.println("Totaly review indexed: " + count);
		
		// Close index writer 
		myiwriter.close();
	}
}