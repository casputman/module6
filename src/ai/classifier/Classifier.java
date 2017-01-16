package ai.classifier;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Florian
 *
 */
public class Classifier {
	
	/**
	 * Set of all existing classes
	 */
	Set<String> classes;
	
	/**
	 * Maps class name to text count.
	 */
	Map<String, Integer> textCounts;
	
	/**
	 * Maps class name to occurrence counts of words.
	 */
	Map<String, Map<String, Integer>> wordCounts;
	

	public Classifier(Map<String, Collection<String>> texts) {
		classes = texts.keySet();
		
		TextProcessor processor = new TextProcessor();
		for (String className : classes) {
			int textCount = 0;
			
			for (String text : texts.get(className)) {
				
			}
		}
		
		
		
	}

}
