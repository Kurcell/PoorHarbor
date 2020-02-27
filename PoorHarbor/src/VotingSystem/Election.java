package VotingSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Election {
	
	public static void main(String[] args) throws IOException {
	File f = new File("results/results.txt");
	f.delete(); // deletes previous results if any
	f.getParentFile().mkdirs();
	f.createNewFile();
	Scanner fileIn = new Scanner(new File("results/nothingToSeeHere.txt"));
	String results = "";
	while((fileIn.hasNextLine())) {
		results+=(fileIn.nextLine());
	}
	fileIn.close();
	Files.write(Paths.get("results/results.txt"), results.getBytes());
	}
}
