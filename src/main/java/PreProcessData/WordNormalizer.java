package PreProcessData;

import Classes.Stemmer;
/**
 * This is for INFSCI-2140 in 2020
 *
 */
public class WordNormalizer {
	
	// YOU CAN ADD ESSENTIAL PRIVATE METHODS OR VARIABLES ->
	

	// YOU MUST IMPLEMENT THIS METHOD -> 
	public char[] lowercase(char[] chars) {
		if(chars == null || chars.length == 0) {
			return null;
		}
		
		for(int i=0; i<chars.length; i++) {
			// Transform the uppercase characters in the word to lowercase
			chars[i] = Character.toLowerCase(chars[i]);
		}
		
		return chars;
	}

	// YOU MUST IMPLEMENT THIS METHOD -> 
	public String stem(char[] chars) {
		// Use the stemmer in Classes package to do the stemming on input word, and return the stemmed word
		if(chars == null || chars.length == 0) {
			return "";
		}
		
		String str = "";
		
		Stemmer s = new Stemmer();
		s.add(chars, chars.length);
		s.stem();
		
		str = s.toString();
		
		return str;
	}
}