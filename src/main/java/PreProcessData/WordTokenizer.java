package PreProcessData;

import java.util.ArrayList;

/**
 * This is for INFSCI-2140 in 2020
 *
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	
	// YOU CAN ADD ESSENTIAL PRIVATE METHODS OR VARIABLES ->
	public static final String NON_DIGIT_OR_LETTER_PATTERN = "([[^0-9]&&[^a-z]&&[^A-Z]]+)"; // Remove non-digit or non-letter characters
	
	private ArrayList<String> tokensList; // Store all tokens
	private int wordCount = 0;

	// YOU MUST IMPLEMENT THIS METHOD ->
	public WordTokenizer(char[] texts) {
		// This constructor will tokenize the input texts (usually it is a char array for a whole document).
		tokensList = new ArrayList<String>();
		
		String segmentedText = new String(texts).replaceAll(NON_DIGIT_OR_LETTER_PATTERN, " ")
				                                .replaceAll("( )+", " "); // A long string segmented by whitespace
		
		String[] choppedText = segmentedText.split(" "); // Every single word in the long string
		for(String singleWord: choppedText) {
			tokensList.add(singleWord);
		}		
	}

	// YOU MUST IMPLEMENT THIS METHOD ->
	public char[] nextWord() {
		// Read and return the next word of the document
		// or return null if it is the end of the document
		if(tokensList.size() > wordCount) {
			return tokensList.get(wordCount++).toCharArray();
		}
		
		return null;	
	}
	
	/*
	 * tokensList: a, b, c, d, e
	 * 5>0, return a;
	 * 5>1, return b;
	 * 5>2, return c;
	 * 5>3, return d;
	 * 5>4, return e;
	 * 5>5, return false;
	 * 
	 */
}