package IndexingLucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import Classes.Review;

public class MyIndexWriter {
	
	protected File dir;
	private Directory directory;
	private IndexWriter iwriter;
	private FieldType type;
	
	public MyIndexWriter() throws IOException {
		// Specify index repository path
		directory = FSDirectory.open(Paths.get(Classes.Path.INDEX_DIR));  
		
		// Configure lucene index writer
		IndexWriterConfig indexConfig=new IndexWriterConfig(new WhitespaceAnalyzer());
		indexConfig.setMaxBufferedDocs(10000);
		iwriter = new IndexWriter(directory, indexConfig);
		
		// Configure field type
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
		type.setStoreTermVectors(true);
	}
	
	/**
	 * This method build index for each review.
	 * 
	 * NOTE THAT: in your implementation of the index, you should transform string docnos 
	 * into non-negative integer docids 
	 * In MyIndexReader, you should be able to request the integer docid for docnos.
	 * 
	 * @param docno 
	 * @param content
	 * @throws IOException
	 */
	public void index(Object o) throws IOException {
	    Review r = (Review) o;
	    
		Document doc = new Document();
		doc.add(new StringField("id", r.getId(), Field.Store.YES));
//		doc.add(new StringField("restName", r.getRestName(), Field.Store.YES));
		doc.add(new Field("restName", r.getRestName(), type));
		doc.add(new StoredField("restUrl", r.getRestUrl()));
		doc.add(new StoredField("originalContent", r.getOriginalContent()));
		doc.add(new Field("preprocessedContent", r.getPreprocessedContent(), type));
		iwriter.addDocument(doc);
	}
	
	/**
	 * Close the index writer and output all the buffered content (if any).
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		iwriter.close();
		directory.close();
	}
}
