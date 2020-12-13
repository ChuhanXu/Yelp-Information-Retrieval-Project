package Classes;

/**
 * Review class
 * 
 * @author florahan
 */
public class Review {

	String id;                  // Unique identifier of review 
	String restName;            // Name of restaurant
	String restUrl;				// URL of restaurant
	String restDesc;            // Description of restaurant
	String originalContent;     // Original Content of review  
	String preprocessedContent; // Preprocessed content of review
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getRestDesc() {
		return restDesc;
	}

	public void setRestDesc(String restDesc) {
		this.restDesc = restDesc;
	}

	
	public String getOriginalContent() {
		return originalContent;
	}

	public void setOriginalContent(String originalContent) {
		this.originalContent = originalContent;
	}

	public String getPreprocessedContent() {
		return preprocessedContent;
	}

	public void setPreprocessedContent(String preprocessedContent) {
		this.preprocessedContent = preprocessedContent;
	}

	@Override
	public String toString() {
		return id + ": " + restName + ", " + restUrl + ", " + restDesc + ", " + originalContent + ", " + preprocessedContent;
	}
}
