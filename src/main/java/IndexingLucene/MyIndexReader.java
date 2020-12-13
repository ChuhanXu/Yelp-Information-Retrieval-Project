package IndexingLucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 * Class for reading index.
 */
public class MyIndexReader {
	
	protected File dir;
	private Directory directory;
	private DirectoryReader ireader;
	private IndexSearcher isearcher;
	private CollectionStatistics ics;
	
	public MyIndexReader() throws IOException {
		directory = FSDirectory.open(Paths.get(Classes.Path.INDEX_DIR)); 
		ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);
		ics = isearcher.collectionStatistics("preprocessedContent");
	}
	
	/**
	 * Get the (non-negative) integer docid for the requested docno.
	 * If -1 returned, it indicates the requested docno does not exist in the index.
	 * NOTE: The integer id is assigned by Lucene.
	 * 
	 * @param docno
	 * @return
	 * @throws IOException 
	 */
	public int getDocid(String docno) throws IOException {
		// Construct a query with field and keyword
		Query query = new TermQuery(new Term("id", docno));
		
		// Get a list of hits and specify the number of returned docs
		TopDocs tops= isearcher.search(query,1);
		
		// Get docs integer id, which is assigned by Lucene itself
		return tops.scoreDocs[0].doc;
	}
	
	/**
	 * Retrive the docno for the integer docid.
	 * 
	 * @param docid
	 * @return
	 * @throws IOException 
	 */
	public String getDocno(int luceneID) throws IOException {
		// Get doc with indicated docID
		Document doc = ireader.document(luceneID);
		
		// Get doc's id(like, in our case, 1-1, 1-2, 1-3...)
		return (doc == null) ? null : doc.get("id");
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] getPostingList(String token) throws IOException {
		Term tm = new Term("preprocessedContent", token);
		int df = ireader.docFreq(tm);
		if(df==0)
			return null;
		Query query = new TermQuery(tm);
		TopDocs tops= isearcher.search(query,df);		
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		int[][] posting = new int[df][];
		int ix = 0;
		Terms vector;
		TermsEnum termsEnum;
		BytesRef text;
		for (ScoreDoc score : scoreDoc){
			int id = score.doc;
			int freq=0;
			vector = ireader.getTermVector(id, "preprocessedContent");
			termsEnum = vector.iterator();
			while ((text = termsEnum.next()) != null) {
			    if(text.utf8ToString().equals(token))
			    	freq+= (int) termsEnum.totalTermFreq();
			}
			posting[ix] = new int[] { id, freq };
			ix++;
		}
		return posting;
	}
	
	/**
	 * Return the number of documents that contains the token.
	 * 
	 * @param token
	 * @return
	 */
	public int DocFreq(String token) throws IOException {
		Term tm = new Term("preprocessedContent", token);
		int df = ireader.docFreq( tm );
		return df;
	}
	
	/**
	 * Return the total number of times the token appears in the collection.
	 * 
	 * @param token
	 * @return
	 */
	public long CollectionFreq(String token) throws IOException {
		// you should implement this method.
		Term tm = new Term("preprocessedContent", token);
		long ctf=ireader.totalTermFreq(tm);
		return ctf;
	}
	
	/**
	 * Get the length of the requested document. 
	 * 
	 * @param docid
	 * @return
	 * @throws IOException
	 */
	public int docLength(int luceneID) throws IOException {
		int docLength = 0;
		Terms vector = ireader.getTermVector(luceneID, "preprocessedContent");
		TermsEnum termsEnum = vector.iterator();
		BytesRef text;
		while ((text = termsEnum.next()) != null) {
			docLength += (int) termsEnum.totalTermFreq();
		}
		return docLength;
	}
	
	public long getCorpusSize() {
		return ics.sumTotalTermFreq();
	}
	
	public void close() throws IOException {
		ireader.close();
		directory.close();
	}
}
