package VotingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Lists.ArrayList;
import Lists.List;

public class Election {

	public static void main(String[] args) throws IOException {
		int numBallots = 0;
		int numBlank = 0;
		int numInvalid = 0;
		List<String> rounds = new ArrayList<String>(1);

		List<Candidate> candidates = new ArrayList<Candidate>(1);

		Scanner can = new Scanner(new File("input/candidates.csv"));
		while(can.hasNextLine()) {
			Candidate newCandidate = new Candidate(can.nextLine());
			candidates.add(newCandidate);
		}
		can.close();

		Scanner ballots = new Scanner(new File("input/ballots.csv"));
		while(ballots.hasNextLine()) {
			Ballot newBallot = new Ballot(ballots.nextLine());
			numBallots++;
			if(newBallot.isBlank()) {
				numBlank++;
			}else if(!newBallot.isValid()) {
				numInvalid++;
			}else {
				for(Candidate c : candidates) {
					if(newBallot.getFirstChoice() == c.getID()) {
						c.getBallots().add(newBallot);
						break;
					}
				}
			}
		}
		ballots.close();

		List<String> lines = new ArrayList<String>(7);
		lines.add("Number of ballots:" + numBallots); 
		lines.add("Number of blank ballots: " + numBlank);
		lines.add("Number of invalid ballots: " + numInvalid);

		File out = new File("output/results.txt");

		out.delete(); // deletes previous results if any
		out.getParentFile().mkdirs();
		out.createNewFile();
		FileWriter results = new FileWriter("output/results.txt");
		for(String s : lines) {
			results.write(s+"\n");
		}
		results.close();
	}
}
