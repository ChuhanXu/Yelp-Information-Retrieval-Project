package PseudoRFSearch;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import Classes.Document;
import Classes.Path;
import Classes.Query;
import IndexingLucene.MyIndexReader;
import SearchLucene.QueryRetrievalModel;

/**
 * Retrieval model enhanced by pseudo relevance feedback. Thread: 
 * (1) You should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents. 
 *     Here all feedback docs are treated as one big pseudo document!
 * (2) Implement GetTokenRFScore method to get each query token's P(token|feedback model) in feedback documents.
 * (3) Implement the relevance feedback model for each token: combine the each query token's original retrieval score
 *     P(token|document) with its score in feedback documents P(token|feedback model)
 * (4) For each document, use the query likelihood language model to get the whole query's new score, 
 *     P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
 *     
 * @author florahan
 */
public class PseudoRFRetrievalModel {

	private static final double MU = 2000.0;
	private static long CORPUS_SIZE;
	
	private MyIndexReader myireader;
	private Directory directory;
	private DirectoryReader dreader;
	
	private Map<Integer, HashMap<String, Integer>> queryResult; // <docid, <term, tf>>
	private Map<String, Long> termFreq; // <term, cf>
	
	public PseudoRFRetrievalModel(MyIndexReader myireader) throws IOException {
		directory = FSDirectory.open(Paths.get(Path.INDEX_DIR));
		dreader = DirectoryReader.open(directory);
		this.myireader = myireader;
		CORPUS_SIZE = myireader.getCorpusSize();
	}

	/**
	 * Search for the topic with pseudo relevance feedback in 2020 Fall assignment 4.
	 * Returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * This method will return the retrieved result of the given query, and this result is enhanced with pseudo RF.
	 * 
	 * @param aQuery The query to be searched for
	 * @param TopN The maximum number of returned documents
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 * @throws Exception
	 */
	public List<Classes.Document> RetrieveQuery(Classes.Query cQuery, int TopN, int TopK, double alpha) throws Exception { 
		// Get P(token|feedback model)
		HashMap<String, Double> TokenRFScore = GetTokenRFScore(cQuery, TopK);

		// Store sorted top N results (from most relevant to least)
		List<Classes.Document> rankedList = new ArrayList<>();
		
		// Get query's content as input into retrieval model
		String[] tokens = cQuery.getQueryContent().split(" ");
		List<DocScore> docScoreList = new ArrayList<>();
		queryResult.forEach((luceneID, tfMap) -> {
		     int doclen = 0;
		     double score = 1.0;
		     try {
		         doclen = myireader.docLength(luceneID);
		     } catch (Exception e) {
		    	 e.printStackTrace();
		     };
		    
		     /**
			  * Dirichlet Prior Smoothing:
			  * p(w|D) = (|D|/(|D|+MU))*(c(w,D)/|D|) + (MU/(|D|+MU))*p(w|REF)
		      * score = λ*p_doc + (1-λ)*p_ref
		      *       = c1*p_doc + c2*p_ref
			  */
		     double c1 = doclen / (doclen + MU);
		     double c2 = MU / (doclen + MU);
		     for(String token : tokens) {
		    	 long cf = termFreq.get(token);
		    	 if(cf == 0) {	
		    		 continue;
		    	 }
		         
		    	 int tf = tfMap.getOrDefault(token, 0);
		    	 double p_doc = (double) tf / doclen; // c(w, D)
		    	 double p_ref = (double) cf / CORPUS_SIZE; // p(w|REF)
		    	 
		    	 // Get the final probability of each token with pseudo RF score
		    	 score *= alpha * (c1 * p_doc + c2 * p_ref) + (1 - alpha) * TokenRFScore.get(token);
		     }
		      
		     DocScore docScore = new DocScore(luceneID, score);
		     docScoreList.add(docScore);
		}); // The end of forEach loop block

		// Sort the result List 
		Collections.sort(docScoreList, new Comparator<DocScore>() {
			@Override
			public int compare(DocScore ds1, DocScore ds2) {
				if(ds1.score != ds2.score) {
					return ds1.score < ds2.score ? 1 : -1;
				} else {
					return 1;
				}
			}
		});

		// Put all documents into result list
		for(int n = 0; n < TopN; n++) {
		     DocScore docScore = docScoreList.get(n);
		     Classes.Document doc = null;
		     try {
		    	 int luceneID = docScore.getId(); // Get Lucene id
		    	 doc = new Document(Integer.toString(luceneID), 
		    			  			myireader.getDocno(luceneID), 
		    			            docScore.getScore(),
		    			            dreader.document(luceneID).get("restName"),
		    			            dreader.document(luceneID).get("restUrl"),
		    			            dreader.document(luceneID).get("originalContent"));
		     } catch(Exception e) {
		    	 e.printStackTrace();
		     };
		     
		     rankedList.add(doc);
		}

		return rankedList;
	}

	/**
	 * For each token in the query, calculate token's score in feedback documents: 
	 * P(token|feedback documents). Use Dirichlet smoothing.
	 * 
	 * @param aQuery
	 * @param TopK
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Double> GetTokenRFScore(Query aQuery, int TopK) throws Exception {
		// Store <token, score> for being returned
		HashMap<String, Double> TokenRFScore = new HashMap<>();
		
		// Get tokens in the given query
	    String[] tokens = aQuery.getQueryContent().split(" ");
	    
	    // Get feedback docs by the given query using feedback retrieval model
	    List<Classes.Document> feedbackDocs = new QueryRetrievalModel().retrieveQuery(aQuery, TopK);

	    // Initiate qeuryResult map <docid, <term, tf>> and termFreq map <term, cf>
	    queryResult = new HashMap<>();
	    termFreq = new HashMap<>();

	    // Search each token appearing in the given query 
	    for(String token : tokens) {
	    	// Determine whether the current token exists in collection 
	    	long cf = myireader.CollectionFreq(token);
	    	termFreq.put(token, cf);
	    	if(cf == 0) {
	    		continue;  
	    	}
	        
	    	int[][] postingList = myireader.getPostingList(token);
	    	for(int[] posting : postingList) {
	    		if(!queryResult.containsKey(posting[0])) { // posting[0]: docid
	    			HashMap<String, Integer> tfMap = new HashMap<>();
	    			tfMap.put(token, posting[1]); // posting[1]: tf
	    			queryResult.put(posting[0], tfMap);
	    		} else {
	    			queryResult.get(posting[0]).put(token, posting[1]);
	    		}
	    	}
	    }
	    /*
		 * After the for loop, we have the map <docid, <term, term_freq>> for each doc to all terms in the query
		 * tokens = [Hong, Kong, economics, Singapore]
		 * 
		 * queryResult : <docid, <term, term_freq>>
		 *                    1,  hong, 15
		 *                        Singapore, 3
		 *                    3,  Singapore, 1
		 *                        kong, 10
		 *                        economics, 30
		 *                  ...,  ...
		 */	

	    // Here all feedback docs are treated as one big pseudo document!!!!!!
	    int len = 0;
	    // Store <term, tf>
	    Map<String, Integer> pseudoDoc = new HashMap<>();
	    for(Classes.Document doc : feedbackDocs) {
	    	queryResult.get(Integer.parseInt(doc.getLuceneID())).forEach((term, tf) -> { // Iterate each tfMap
	    		if(!pseudoDoc.containsKey(term)) {
	    			pseudoDoc.put(term, tf);
	    		} else {
	    			pseudoDoc.put(term, tf + pseudoDoc.get(term));
	    		}
	    	});
	    	
	    	// Accumulate by the current doc's length
	    	len += myireader.docLength(Integer.parseInt(doc.getLuceneID()));
	    }
	  
	    /**
		  * Dirichlet Prior Smoothing:
		  * p(w|D) = (|D|/(|D|+MU))*(c(w,D)/|D|) + (MU/(|D|+MU))*p(w|REF)
	      * score = λ*p_doc + (1-λ)*p_ref
	      *       = c1*p_doc + c2*p_ref
		  */
	    int pseudoLen = len;
	    double c1 = pseudoLen / (pseudoLen + MU);
	    double c2 = MU / (pseudoLen + MU);
	    // Calculate the probability of pseudoDoc generating each term
	    pseudoDoc.forEach((token, tf) -> {
	    	long cf = termFreq.get(token);
	    	double pDoc = (double) tf / pseudoLen; // p(w|D)
	    	double pRef = (double) cf / CORPUS_SIZE; // p(w|REF)
	    	double score = c1 * pDoc + c2 * pRef;
	    	
	    	// Get the RF probability of each token  
	    	TokenRFScore.put(token, score); 
	    });

		return TokenRFScore;
	}
	
	/**
	 * Store a doc with its docid and corresponding score.
	 */
	private class DocScore {
	    
		private int docid;
	    private double score;

	    public DocScore(int docid, double score) {
	      this.docid = docid;
	      this.score = score;
	    }

	    public int getId() {
	      return this.docid;
	    }

	    public double getScore() {
	      return this.score;
	    }
	} 
}