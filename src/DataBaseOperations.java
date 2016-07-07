import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa koja nudi funckije za komunikaciju s bazom. Baza je predstavljena kao
 * skup tri datoteke.
 * 
 * @author Ana Marija Selak
 *
 */
public class DataBaseOperations {

	static List<String> getHierarchy(String path) throws IOException {

		return getLines(Paths.get(path));

	}

	static List<String> getFavourites(String path) {
		return getLines(Paths.get(path));
	}

	static List<String> getLines(Path path) {

		try {
			return Files.readAllLines(path, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	static void writeHierarchy(String path, List<String> lines) {

		writeLines(path, lines);
	}

	static void writeFavourites(String path, List<String> lines) {
		writeLines(path, lines);
	}

	static void writeLines(String path, List<String> lines) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String line : lines) {
			try {
				writer.write(line);
				writer.write(System.getProperty("line.separator"));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<String> getFiles(String path) throws IOException {
		return getLines(Paths.get(path));
	}

	public static String parsePath(String path, List<String> favouriteFiles,
			List<String> hierarchy, List<String> files) {

		File folder = new File(path);
		if (!folder.exists()) {
			return "Folder with specified path does not exist!";
		}
		if (!folder.isDirectory()) {
			return "Given path does not represent a folder! Please enter a path to a folder.";
		}

		File[] listOfFiles = folder.listFiles();
		List<String> newFiles = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				newFiles.add(listOfFiles[i].getName());
			}
		}

		clearDataBase(favouriteFiles, files, hierarchy);

		createNewDataBase(files, hierarchy, newFiles, path);

		return "OK";

	}

	private static void createNewDataBase(List<String> files,
			List<String> hierarchy, List<String> newFiles, String path) {

		createFiles(files, newFiles);
		createHierarchy(hierarchy, path);

	}

	private static void createHierarchy(List<String> hierarchy, String path) {
		System.out.println(path);
		String[] partsOfPath = path.split("\\\\");
		writeHierarchy("Hierarchy.txt", Arrays.asList(partsOfPath));
		try {
			hierarchy.addAll(getHierarchy("Hierarchy.txt"));

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private static void createFiles(List<String> files, List<String> newFiles) {
		files.addAll(newFiles);
		writeLines("Files.txt", newFiles);

	}

	private static void clearDataBase(List<String> favouriteFiles,
			List<String> files, List<String> hierarchy) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File("Favourites.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.print("");
		writer.close();
		favouriteFiles.clear();

		files.clear();
		hierarchy.clear();

	}
}
