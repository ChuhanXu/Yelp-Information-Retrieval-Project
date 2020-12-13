package PreProcessData;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import Classes.*;
/**
 * This is for INFSCI-2140 in 2020
 *
 */
public class StopWordRemover {
	
	// YOU CAN ADD ESSENTIAL PRIVATE METHODS OR VARIABLES ->
	private BufferedReader br = null;
	private HashSet<String> stopWordsSet = null; // Store stemmed stopwords

	// YOU MUST IMPLEMENT THIS METHOD ->
	public StopWordRemover() throws IOException {
		// Load and store the stop words from the FileInputStream with appropriate 
		// data structure that you believe is suitable for matching stop words. 
		// Address of stopword.txt should be Path.StopwordDir
		FileInputStream fis = new FileInputStream(Path.STOPWORD_DIR); // Read in the stopwords list
		InputStreamReader isr = new InputStreamReader(fis);
		br = new BufferedReader(isr);
		
		stopWordsSet = new HashSet<String>();
		String line = "";
		while((line = br.readLine()) != null) {
			char[] charLine = line.toCharArray();
			Stemmer s = new Stemmer(); // Stopwords should be stemmed in consistency with stemmed docs 	
			s.add(charLine, charLine.length);
			s.stem();
			String stemmedLine = s.toString();
			stopWordsSet.add(stemmedLine); // Add all stemmed stop words into the hashset
		}	
	}

	// YOU MUST IMPLEMENT THIS METHOD ->
	public boolean isStopword(char[] word) {
		// Return true if the input word is a stopword, or false if not
		if(word == null || word.length == 0) { 
			return false;
		}
	
		String stopword = new String(word);
		if(stopWordsSet.contains(stopword)) {
			return true;
		}
		
		return false;
	}
}