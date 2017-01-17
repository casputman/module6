package ai;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ai.classifier.Classifier;

/**
 * Responsible for starting the program
 *
 */
public class AI {
	static String BASE_PATH = "C:\\Users\\Florian\\Desktop\\blogs";
	
	public static void main(String[] args) throws IOException {
		Map<String, Collection<String>> texts = new HashMap<String, Collection<String>>();
		
		texts.put("Female", FileProcessor.importFiles(Paths.get(BASE_PATH, "F").toString()));
		texts.put("Male", FileProcessor.importFiles(Paths.get(BASE_PATH, "M").toString()));
		
		Classifier classifier = new Classifier(texts);
		
		System.out.println(classifier.getWordCounts());
		System.out.println(classifier.getChiSquared());
	}
	
	public AI() {
		// TODO Auto-generated constructor stub
	}

}
