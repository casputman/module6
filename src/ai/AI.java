package ai;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for starting the program
 * @author Florian
 *
 */
public class AI {
	static String BASE_PATH = "C:\\Users\\Florian\\Desktop\\blogs";
	
	public static void main(String[] args) throws IOException {
		Map<String, List<String>> texts = new HashMap<String, List<String>>();
		
		texts.put("Female", FileProcessor.importFiles(Paths.get(BASE_PATH, "F").toString()));
		texts.put("Male", FileProcessor.importFiles(Paths.get(BASE_PATH, "M").toString()));
	}
	
	public AI() {
		// TODO Auto-generated constructor stub
	}

}
