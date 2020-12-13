package Classes;

/**
 * Query with corresponding id and content.
 * NOTE: YOU CAN MODIFY THIS CLASS.
 * 
 * @author florahan
 */
public class Query {

	private String topicId;	     // Query id
	private String queryContent; // Query content 	
	
	public String getTopicId() {
		return topicId;
	}
	
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	public String getQueryContent() {
		return queryContent;
	}
	
	public void setQueryContent(String queryContent) {
		this.queryContent = queryContent;
	}	
}