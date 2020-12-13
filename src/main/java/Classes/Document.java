package Classes;

/**
 * Document with corresponding docid(integer id), docno(string id), 
 * score, restaurant's name, restaurant's url and original content.
 */
public class Document {
	
	private String luceneID; // doc's id assigned by Lucene
	private String docID; // doc's id defined by our own
	private double score; // doc's score
	private String restName;  
	private String restUrl;
	private String originalContent;
	
	public Document(String luceneID, String docID, double score, String restName, String restUrl, String originalContent) {
		this.luceneID = luceneID;
		this.docID = docID;
		this.score = score;
		this.restName = restName;
		this.restUrl = restUrl;
		this.originalContent = originalContent;
	}

	public String getLuceneID() {
		return luceneID;
	}

	public void setLuceneID(String luceneID) {
		this.luceneID = luceneID;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public String getOriginalContent() {
		return originalContent;
	}

	public void setOriginalContent(String originalContent) {
		this.originalContent = originalContent;
	}
	
	@Override
	public String toString() {
		return luceneID + ", " + docID + ", " + score + ", " + restName + ", " + restUrl + ", " + originalContent;
	}
}
