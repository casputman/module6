package ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Reads a file and converts the file to a string.
 *
 */
public class FileProcessor {
	public static List<String> importFiles(String path) throws IOException {
		List<String> fileStrings = new ArrayList<String>();
		
		try(Stream<Path> paths = Files.walk(Paths.get(path))) {
		    paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		            try {
						fileStrings.add(new String(Files.readAllBytes(filePath)));
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		    });
		}
		
		return fileStrings;
	}
	
	public static Map<String, String> importFilesWithPath(String path) throws IOException {
		Map<String, String> fileStrings = new HashMap<String, String>();
		
		try(Stream<Path> paths = Files.walk(Paths.get(path))) {
		    paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		            try {
						fileStrings.put(filePath.toString(), new String(Files.readAllBytes(filePath)));
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		    });
		}
		
		return fileStrings;
	}

}
