package ai.classifier.textProcessor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class TextProcessor {
	public static final int MIN_WORD_SIZE = 2;
	
	Collection<String> stopWords;
	
	public TextProcessor() {
		stopWords = new HashSet<String>();
		
		InputStream input = getClass().getResourceAsStream("stopWords.txt");
		if (input != null) {
			Scanner sc = new Scanner(input);
			
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				stopWords.add(line);
			}
			
			sc.close();
		}
	}

	/**
	 * Takes a string (raw text) and turns it into a list of cleaned up words.
	 * I.E. "Alcohol in the human body:   Ethanol, or more" -> {alcohol, in, the, human, body, ethanol, or, more}
	 */
	public List<String> process(String text) {
		String[] words = text.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
		List<String> arrayList = new ArrayList<>(); 
		Collections.addAll(arrayList, words);
		
		// Loop over all words in our ArrayList to filter out non-eligible words
		ListIterator<String> it = arrayList.listIterator();
		while (it.hasNext()) {
			String str = it.next();
			
			/*
			 * Filter on the following cases:
			 *  - Word too small (defined in MIN_WORD_SIZE)
			 *  - Word part of stop word list
			 */
			if (str.length() < MIN_WORD_SIZE ||
					stopWords.contains(str)) {
				it.remove();
			}
		}
		
		return arrayList;
	}
}
