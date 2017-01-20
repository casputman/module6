package fullTests;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ai.FileProcessor;
import ai.classifier.Classifier;

public class BlogTest {
	Classifier classifier;
	Map<String, Collection<String>> texts;
	
	public static final String BASE_PATH = "C:\\Users\\Florian\\Desktop\\blogs";

	
	public BlogTest() throws IOException {
		texts = new HashMap<String, Collection<String>>();
		
		texts.put("Female", FileProcessor.importFiles(Paths.get(BASE_PATH, "F").toString()));
		texts.put("Male", FileProcessor.importFiles(Paths.get(BASE_PATH, "M").toString()));
	}
	
	@Before
	public void initClassifier() {
		classifier = new Classifier(texts);
	}

	@Test
	public void femaleTest() throws IOException {
		Collection<String> testTexts = FileProcessor.importFiles(Paths.get(BASE_PATH, "test", "F").toString());
		
		Map<String, Integer> results = new HashMap<String, Integer>();
		for (String testText : testTexts) {
			String result = classifier.apply(testText);
			
			if (!results.containsKey(result)) {
				results.put(result, 0);
			}
			
			results.put(result, results.get(result) + 1);
		}
		
		System.out.println("Female test result: " + results);
	}
	
	@Test
	public void maleTest() throws IOException {
		Collection<String> testTexts = FileProcessor.importFiles(Paths.get(BASE_PATH, "test", "M").toString());
		
		Map<String, Integer> results = new HashMap<String, Integer>();
		for (String testText : testTexts) {
			String result = classifier.apply(testText);
			
			if (!results.containsKey(result)) {
				results.put(result, 0);
			}
			
			results.put(result, results.get(result) + 1);
		}
		
		System.out.println("Male test result: " + results);
	}
}
