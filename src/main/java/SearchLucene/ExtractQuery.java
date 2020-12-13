package SearchLucene;

import java.io.IOException;

import Classes.Query;
import PreProcessData.StopWordRemover;
import PreProcessData.WordNormalizer;
import PreProcessData.WordTokenizer;

/**
 * Extract query(equals to QueryParser)
 * NOTE: the query content should be 1) tokenized, 2) to lowercase, 3) remove stop words, 4) stemming
 */
public class ExtractQuery {

	Query query; // A query object(defined by ourself)

	/**
	 * Conctruct a query 
	 * @throws IOException 
	 */
	public ExtractQuery(String text) throws IOException {	
		
		WordTokenizer tokenizer = new WordTokenizer(text.toCharArray());
		StopWordRemover stopWordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();
		
		String queryContent = "";
		char[] word = {};
		while((word = tokenizer.nextWord()) != null) {
			word = normalizer.lowercase(word);
			if(!stopWordRemover.isStopword(word)) {
				queryContent += normalizer.stem(word) + " ";
 			}
		}
		
		query  = new Query();
		query.setQueryContent(queryContent);
	}
	
	/**
	 * Get a query after being preprocessed
	 * @return
	 */
	public Query getQuery() {
		return query;
	}
}
