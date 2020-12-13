package SearchLucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import Classes.*;
import IndexingLucene.MyIndexReader;

public class QueryRetrievalModel {

	private Directory directory;
	private DirectoryReader dreader;
	private IndexSearcher isearcher;

	public QueryRetrievalModel() throws IOException {
		directory = FSDirectory.open(Paths.get(Path.INDEX_DIR));
		dreader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(dreader);
	}

	/**
	 * Conduct retrieving given a query.
	 * NOTE: Here, self-defined Query class would conflict with Lucene Query class,
	 * so we have to use the full name of the Query class defined by our own.
	 * 
	 * @param cQuery
	 * @param TopN
	 * @return
	 * @throws Exception
	 */
	public List<Classes.Document> retrieveQuery(Classes.Query cQuery, int TopN) throws Exception {
		// Store returned docs
		List<Classes.Document> rankedList = new ArrayList<>();
		Query query = new QueryParser("preprocessedContent", new WhitespaceAnalyzer()).parse(cQuery.getQueryContent()); // Return a query object
		ScoreDoc[] scoreDocs = isearcher.search(query, TopN).scoreDocs;
		for(ScoreDoc hit : scoreDocs) {		
			rankedList.add(new Classes.Document(
				hit.doc + "", // doc'id assigned by Lucene 
				dreader.document(hit.doc).get("id"), // doc'id defined by our own
				hit.score, // doc' relevance score assigned by Lucene
				dreader.document(hit.doc).get("restName"), // doc's restaurant name
				dreader.document(hit.doc).get("restUrl"), // doc's restaurant url
				dreader.document(hit.doc).get("originalContent") // doc's original content
			));
		}
		
		return rankedList;
	}
}
