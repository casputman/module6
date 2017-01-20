package ai.classifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ai.classifier.textProcessor.TextProcessor;

/**
 * 
 * @author Florian
 *
 */
public class Classifier {
	public static final int DEFAULT_VOCABULARY_SIZE = 100;
	public static final double DEFAULT_K = 1;
	
	private static double log2(double x) {
	    return Math.log(x) / Math.log(2);
	}
	
	private final int FEATURE_SET_SIZE;
	private final double K;
	
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
	 * Maps words to their chiSquareValues.
	 */
	Map<String, Double> chiSquared;
	
	/**
	 * Contains all words in the vocabulary and their respective chances per class.
	 */
	Set<String> vocabulary;
	
	/**
	 * Contains prior values for every class in log2.
	 */
	Map<String, Double> prior;
	
	/**
	 * Contains a mapping of class -> (word -> condProb in log2).
	 */
	Map<String, Map<String, Double>> conditionalProbabilities;
	
	/**
	 * Constructs a classifier with given training set.
	 * @param texts Should consist of a mapping of class name to texts associated with the class
	 */
	public Classifier(Map<String, Collection<String>> texts) {
		this(texts, DEFAULT_VOCABULARY_SIZE, DEFAULT_K);
	}
	
	/**
	 * Constructs a classifier with given training set and given parameters vocabulary size and smoothing factor K.
	 * @param texts
	 * @param vocabularySize
	 * @param k
	 */
	public Classifier(Map<String, Collection<String>> texts, int vocabularySize, double k) {
		// Initialize some stuff
		FEATURE_SET_SIZE = vocabularySize;
		K = k;
		
		classes = new HashSet<String>();
		textProcessor = new TextProcessor();
		
		textCounts = new HashMap<String, Integer>();
		wordCounts = new HashMap<String, Map<String, Integer>>();
		prior = new HashMap<String, Double>();
		conditionalProbabilities = new HashMap<String, Map<String, Double>>();
		
		texts.keySet().stream().forEach(className -> addClass(className));
		trainBulk(texts);
	}
	
	public void addClass(String className) {
		classes.add(className);
		wordCounts.put(className, new HashMap<String, Integer>());
		conditionalProbabilities.put(className, new HashMap<String, Double>());
	}
	
	public String apply(String text) {
		/*
		 * We will be maximizing log(P(c)) + sum over words w in text (log(P(w|c))
		 */
		
		List<String> cleanText = textProcessor.process(text);
		
		Double maxScore = null;
		String maxClass = null;
		
		for (String className : classes) {
			double score = prior.get(className);
			
			for (String word : cleanText) {
				score += catchNullNumber(conditionalProbabilities.get(className).get(word));
			}
			
			if (maxScore == null || score > maxScore) {
				maxScore = score;
				maxClass = className;
			}
		}
		
		return maxClass;
	}
	
	private double calcChiSquare(String word) {
		double chiSquareValue = 0;
		
		// Loop over word/not word. i = 0 will represent word, i = 1 not word.
		for (int i = 0; i <= 1; i++) {
			for (String className : classes) {
				double wordCount = catchNullNumber(wordCounts.get(className).get(word));
				
				// This corrects for word/not word. If i = 0, we will get 'word' count, if i = 1 we will get 'not word' count
				double actual = i == 0 ? wordCount : textCounts.get(className) - wordCount;
				double expected = calcExpectedValue(className, word, i == 0);

				chiSquareValue += Math.pow(actual - expected, 2) / expected;
			}
		}
		
		return chiSquareValue;
	}
	
	private double calcExpectedValue(String className, String word, boolean present) {
		/*
		 * We calculate Wi x Cj / N
		 * Or 'the sum of word occurrences in all classes' x 'the sum of texts in class' / 'total texts'
		 */
		
		// Start at word occurrences in all classes
		double wordCount = 0;
		for (String c : classes) {
			int occurrences = catchNullNumber(wordCounts.get(c).get(word));
			if (present) {
				wordCount += occurrences;
			} else {
				wordCount += textCounts.get(c) - occurrences;
			}
		}
		
		// Then get the sum of texts in class
		double textsInClass = textCounts.get(className);
		
		// Finally get total text count
		double totalTexts = textCounts.values().stream().mapToInt(i -> i).sum();
		return wordCount * textsInClass / totalTexts;
	}
	
	/**
	 * Returns 0 if argument is zero, otherwise returns argument.
	 * @param num
	 * @return
	 */
	private double catchNullNumber(Double num) {
		return num == null ? 0 : num;
	}

	/**
	 * Returns 0 if argument is zero, otherwise returns argument.
	 * @param num
	 * @return
	 */
	private int catchNullNumber(Integer num) {
		return num == null ? 0 : num;
	}
	
	public Map<String, Double> getChiSquared() {
		return chiSquared;
	}
	
	public Set<String> getClasses() {
		return classes;
	}
	
	public Map<String, Integer> getTextCounts() {
		return textCounts;
	}

	public Map<String, Map<String, Integer>> getWordCounts() {
		return wordCounts;
	}
	
	/**
	 * Add specified text to the training set and trains the classifier based on a text belonging to given class.
	 * @param className Class the text belongs to. The class should already exist in the classifier.
	 * @param text Text to (re-)train the classifier with
	 */
	public void train(String className, String text) {
		train(className, text, true);
	}
	
	private void train(String className, String text, boolean update) {
		// Increment text count
		Integer textCount = catchNullNumber(textCounts.get(className));
		textCounts.put(className, textCount + 1);
		
		// Process the text
		List<String> words = textProcessor.process(text);
		Map<String, Integer> classWordCounts = wordCounts.get(className);
		
		// For every word, add one to the count of that word
		for (String word : words) {
			Integer count = classWordCounts.get(word);
			count = count == null ? 0 : count; // Catch null
			
			classWordCounts.put(word, count + 1);
		}
		
		if (update) {
			update();
		}
	}
	
	/**
	 * Trains the classifier based on a map containing classnames and corresponding texts.
	 * @param texts
	 */
	public void trainBulk(Map<String, Collection<String>> texts) {
		texts.keySet().stream().forEach(className -> trainBulk(className, texts.get(className), false));
		update();
	}
	
	/**
	 * Trains the classifier based on a collection of texts belonging to a given class.
	 * @param className
	 * @param texts
	 */
	public void trainBulk(String className, Collection<String> texts) {
		trainBulk(className, texts, true);
	}
	
	private void trainBulk(String className, Collection<String> texts, boolean update) {
		texts.stream().forEach(text -> train(className, text, false));
		
		if (update) {
			update();
		}
	}

	private void update() {
		updateConditionalProbabilities();
	}
	
	private void updateChiSquared() {
		chiSquared = new HashMap<String, Double>();
		wordCounts.values().stream().forEach(
				m -> m.keySet().stream().forEach(
						word -> chiSquared.put(word, calcChiSquare(word))));
	}
	
	private void updateConditionalProbabilities() {
		updateVocabulary();
		
		for (String className : classes) {
			double docsInClass = textCounts.get(className);
			prior.put(className, log2(docsInClass) - log2(textCounts.values().stream()
																	.mapToInt(i -> i)
																	.sum()));
			
			double totalWordOccurrencesInClass = wordCounts.get(className).entrySet().stream()
																	.filter(e -> vocabulary.contains(e.getKey()))
																	.mapToDouble(e -> e.getValue() + K)
																	.sum();
			
			for (String word : vocabulary) {
				double occurrences = catchNullNumber(wordCounts.get(className).get(word));
				
				double condProb = log2(occurrences + K) - log2(totalWordOccurrencesInClass);
				conditionalProbabilities.get(className).put(word, condProb);
			}
		}
	}
	
	private void updateVocabulary() {
		updateChiSquared();
		
		vocabulary = chiSquared.entrySet().stream()
				.sorted((a, b) -> -a.getValue().compareTo(b.getValue()))
				.limit(FEATURE_SET_SIZE)
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	public double getK() {
		return K;
	}

	public int getVocabularySize() {
		return FEATURE_SET_SIZE;
	}
}
