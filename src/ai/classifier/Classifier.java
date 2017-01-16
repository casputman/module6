package ai.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.classifier.textProcessor.TextProcessor;

/**
 * 
 * @author Florian
 *
 */
public class Classifier {
	
	/**
	 * The text processor we will use.
	 */
	TextProcessor textProcessor;
	
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
	
	/**
	 * Constructs a classifier with given training set.
	 * @param texts Should consist of a mapping of class name to texts associated with the class
	 */
	public Classifier(Map<String, List<String>> texts) {
		// Initialize some stuff
		classes = texts.keySet();
		textProcessor = new TextProcessor();
		
		textCounts = new HashMap<String, Integer>();
		wordCounts = new HashMap<String, Map<String, Integer>>();
		
		applyInitialTraining(texts);
	}
	
	private void applyInitialTraining(Map<String, List<String>> texts) {
		for (String className : classes) {
			// Initialize bag of words for class
			wordCounts.put(className, new HashMap<String, Integer>());
			
			for (String text : texts.get(className)) {
				train(className, text);
			}
		}
	}
	
	/**
	 * Trains the classifier based on a text belonging to given class.
	 * @param className Class the text belongs to. The class should already exist in the classifier.
	 * @param text Text to (re-)train the classifier with
	 */
	public void train(String className, String text) {
		// Increment text count
		Integer textCount = textCounts.get(className);
		textCount = textCount == null ? 0 : textCount; // Catch null
		textCounts.put(className, textCount);
		
		// Process the text
		List<String> words = textProcessor.process(text);
		Map<String, Integer> classWordCounts = wordCounts.get(className);
		
		// For every word, add one to the count of that word
		for (String word : words) {
			Integer count = classWordCounts.get(word);
			count = count == null ? 0 : count; // Catch null
			
			classWordCounts.put(word, count + 1);
		}
	}

	public Map<String, Map<String, Integer>> getWordCounts() {
		return wordCounts;
	}
}
