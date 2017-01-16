package ai.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextProcessor implements ITextProcessor {
	List<String> stopWords;

	public TextProcessor(List<String> stopWords) {
		this.stopWords = stopWords;
	}

	/**
	 * Takes a string (raw text) and turns it into a list of cleaned up words.
	 * I.E. "Alcohol in the human body:   Ethanol, or more" -> {alcohol, in, the, human, body, ethanol, or, more}
	 */
	public List<String> process(String text) {
		String[] words = text.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
		List<String> arrayList = new ArrayList<>(); 
		Collections.addAll(arrayList, words);
		for (int i = 0; i < arrayList.size(); i++){
			for (String word : stopWords){
				if (arrayList.get(i).equals(word)){
					arrayList.remove(i);
				}
			}
		}
		return arrayList;
	}
}
