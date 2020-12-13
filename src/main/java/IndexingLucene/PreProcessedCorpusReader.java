package IndexingLucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import Classes.Path;
import Classes.Review;

/**
 * Obtain single review for indexing.
 * 
 * @author florahan
 */
public class PreProcessedCorpusReader {
	
	private BufferedReader br;
	private FileInputStream fis;
	private InputStreamReader isr;
	
	public PreProcessedCorpusReader() throws IOException {
		// This constructor should open the file in Path.DataTextDir
		// and also should make preparation for function nextDocument()
		// remember to close the file that you opened, when you do not use it any more
		fis = new FileInputStream(Path.RESULT_CORPUS);
		isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);   
	}
	
	/**
	 * Get a review.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Object nextDocument() throws IOException {
		String revID = br.readLine();
		if(revID == null) {
			br.close();	
			return null;
		}
	
		String restName = br.readLine();
		String restUrl = br.readLine();
		String originalContent = br.readLine();
		String preprocessedContent = br.readLine();
	
		Review review = new Review();
		review.setId(revID);
		review.setRestName(restName);
		review.setRestUrl(restUrl);
		review.setOriginalContent(originalContent);
		review.setPreprocessedContent(preprocessedContent);

//		System.out.println(review);
		return review;
	}
}
