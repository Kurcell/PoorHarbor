package VotingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Lists.ArrayList;
import Lists.List;
import Sets.DynamicSet;
import Sets.Set;

public class Election {

	public static void main(String[] args) throws IOException {
		int numBallots = 0;
		int numBlank = 0;
		int numInvalid = 0;
		int numCandidates = 0;
		boolean decided = false;
		int round = 1;
		
		List<String> lines = new ArrayList<String>(1);

		List<Candidate> candidates = new ArrayList<Candidate>(1);

		List<Candidate> eliminated = new ArrayList<Candidate>(1);
		
		List<Candidate> leastVotes = new ArrayList<Candidate>(1);

		Scanner can = new Scanner(new File("candidates.csv"));
		while(can.hasNextLine()) {
			Candidate newCandidate = new Candidate(can.nextLine());
			candidates.add(newCandidate);
			numCandidates++;
		}
		can.close();

		Scanner ballots = new Scanner(new File("ballots2.csv"));
		while(ballots.hasNextLine()) {
			Ballot newBallot = new Ballot(ballots.nextLine());
			numBallots++;
			if(!newBallot.isValid()) {
				numInvalid++;
			}else if(newBallot.isBlank()) {
				numBlank++;
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

		int numValid = numBallots - numBlank - numInvalid;

		lines.add("Number of ballots: " + numBallots); 
		lines.add("Number of blank ballots: " + numBlank);
		lines.add("Number of invalid ballots: " + numInvalid);

		while(!decided) { // Election, will continue until decided

			boolean nextRound = true;
			while(nextRound) { // Elimination phase, will refresh every time a round is concluded without a winner

				int eliminate = candidates.size()-1;
				leastVotes.add(candidates.get(eliminate));
				int ones = 0;
				String loserName = "";

				for(int i = 0;i < candidates.size();i++) { // Count the votes and store them in their counting spot
					if(candidates.get(i).getCount(1) < candidates.get(eliminate).getCount(1)) {
						eliminate = i;
						leastVotes.clear();
					    leastVotes.add(candidates.get(i));
					}
					if (candidates.get(i).getCount(1) == candidates.get(eliminate).getCount(1)) {
						leastVotes.add(candidates.get(i));
					}
				}
				if(leastVotes.size() > 1) {
					eliminate = getPositionByID(candidates, tieBreaker(leastVotes, numCandidates));
				}
				if(eliminate == -1) {
					eliminate = candidates.size() - 1;
				}

				eliminated.add(candidates.get(eliminate));
				loserName = candidates.get(eliminate).getName();
				ones = candidates.get(eliminate).getBallots().size();

				for(Ballot b : candidates.get(eliminate).getBallots()) {
					while(getCandidateByID(eliminated,b.getFirstChoice()) != null) {
						b.eliminateCandidate(b.getFirstChoice());
					}
					getCandidateByID(candidates,b.getFirstChoice()).getBallots().add(b);
				}
				candidates.get(eliminate).getBallots().clear();
				candidates.remove(eliminate);

				lines.add("Round " + round + ": " + loserName + " was eliminated with " + ones + " #1's");
				
				for(int i = 0;i<candidates.size();i++) { // Check if anyone won the election
					
					if(candidates.get(i).getBallots().size() > numValid/2) {
						lines.add("Winner: " + candidates.get(i).getName() + " wins with " 
								+ candidates.get(i).getBallots().size() + " #1's");
						decided = true;
						nextRound = false;
					}
				}
				
				round++;

			}
		}

		File out = new File("results.txt");

		out.delete(); // deletes previous results if any
		out.createNewFile();
		FileWriter results = new FileWriter("results.txt");
		for(String s : lines) {
			results.write(s+"\n");
		}
		results.close();
	}

	// Non-member methods
	
	public static Candidate getCandidateByID(List<Candidate> list, int ID) {
		for(Candidate c : list) {
			if(c.getID() == ID) {
				return c;
			}
		}
		return null;
	}
	
	public static int getPositionByID(List<Candidate> list, int ID) {
		for(int i = 0;i<list.size();i++) {
			if(list.get(i).getID() == ID) {
				return i;
			}
		}
		return -1;
	}
	
	public static int tieBreaker(List<Candidate> list, int maximumCheck) {
		int check = 2;
		boolean found = true;
		int eliminate = list.size()-1;
		while(!found && check < maximumCheck) {
			for(int i = 0;i<list.size();i++) {
				if(list.get(i).getCount(check) < list.get(eliminate).getCount(check)) {
					eliminate = i;
					found = true;
				}else if(list.get(i).getCount(check) == list.get(eliminate).getCount(check)) {
					found = false;
				}
			}
			check++;
		}
		if(!found) return -1;
		return list.get(eliminate).getID();
		
	}
	

}
