package VotingSystem;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Lists.ArrayList;
import Lists.List;

public class Election {

	/**
	 * The Election class has 10 private fields:
	 * Six integers to store: the number of ballots, blank ballots, invalid ballots,
	 * valid ballots, candidates and the current round.
	 * Four array lists to store: the results that will be written to the text file "results.txt", 
	 * the candidate objects, the eliminated candidate objects, and the candidate(s)
	 * who have the lowest amount of #1's
	 */
	private int numBallots = 0;
	private int numBlank = 0;
	private int numInvalid = 0;
	private int numValid = 0;
	private int numCandidates = 0;
	private int round = 1;
	private List<String> results = new ArrayList<String>(1);
	private List<Candidate> candidates = new ArrayList<Candidate>(1);
	private List<Candidate> eliminated = new ArrayList<Candidate>(1);
	private List<Candidate> leastVotes = new ArrayList<Candidate>(1);

	/**
	 * The main method begins by declaring a new Election object. It is being declared to facilitate the use of other methods
	 * to complete the process of the election. Then four methods are called in succession. The loadCandidates and loadBallots classes
	 * retrieve all the info necessary to begin an election. The process method completes all the processes of an election and the 
	 * writeResults method writes the results to the text file "results.txt". These are explained further in their own documentation.
	 */
	public static void main(String[] args) throws IOException {
		Election election = new Election();
		election.loadCandidates(new File("candidates.csv"));
		election.loadBallots(new File("ballots.csv"));
		election.process();
		writeResults(election.results); 
	}

	/** Election methods */

	/**
	 * Scans through a given file line by line until there are no more lines. Each line will be passed onto the Candidate 
	 * constructor as a parameter String to declare a new candidate and then add them to Election's candidates array list.
	 */
	public void loadCandidates(File file) throws FileNotFoundException {
		Scanner can = new Scanner(file);
		while(can.hasNextLine()) {
			Candidate newCandidate = new Candidate(can.nextLine());
			candidates.add(newCandidate);
		}
		numCandidates = candidates.size();
		can.close();
	}

	/**
	 * Scans through a given file line by line until there are no more lines. Each line will be passed onto the Ballot
	 * constructor as a parameter String to declare a new ballot and increment the count of ballots. The ballot will be 
	 * checked with the ballot methods isBlank and isValid, if it tests false for both then the ballot gets added to the 
	 * candidate whose ID matches the ballot's first choice. Otherwise the respecting count gets incremented and the
	 * ballot doesn't get added to any candidate. The results of the counts are then added to the results array list.
	 */
	public void loadBallots(File file) throws FileNotFoundException {
		Scanner ballots = new Scanner(file);
		while(ballots.hasNextLine()) {
			Ballot newBallot = new Ballot(ballots.nextLine());
			numBallots++;
			if(newBallot.isBlank()) {
				numBlank++;
			}else if(!newBallot.isValid(candidates)) {
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
		numValid = numBallots - numBlank - numInvalid;
		results.add("Number of ballots: " + numBallots); 
		results.add("Number of blank ballots: " + numBlank);
		results.add("Number of invalid ballots: " + numInvalid);
	}

	/** 
	 * Completes the processes of an election. Starts by checking if there are no ballots, and declaring the winner
	 * to be the lowest ID candidate if that is the case and ends the method. Every iteration of the following while loop
	 *  will eliminate a candidate through the methods eliminateCandidate, taking as parameter the getTrailingCandidate method,
	 * and increment the round integer by 1 to specify the round. The while loop runs until a winner is determined through 
	 * the checkWinner method.
	 */
	public void process() {
		if(numValid == 0) {
			results.add("Winner: " + candidates.get(0).getName() + " wins with " + candidates.get(0).getBallots().size() + " #1's");
			return;
		}
		while(!checkWinner()) {
			eliminateCandidate(getTrailingCandidate());
			round++;
		}
	}

	/**
	 * Loops through all the candidates and returns true if there's a candidate who holds more than half of all
	 * candidates. Otherwise returns false.
	 */
	public boolean checkWinner() {
		for(int i = 0;i<candidates.size();i++) {
			if(candidates.get(i).getBallots().size() > numValid/2) {
				results.add("Winner: " + candidates.get(i).getName() + " wins with " 
						+ candidates.get(i).getBallots().size() + " #1's");
				return true;
			}
		}
		return false;
	}

	/**
	 * Eliminates a candidate from the election. Starts by declaring an int ones and a String loserName, used later for results.
	 * The candidate to be eliminated is then added to the eliminated array list. The method then loops through the ballots of 
	 * the candidate to be eliminated. The inner while loop with use the ballot method eliminateCandidate to remove the first choice
	 * until the first choice is still in the election. After this, the eliminated candidate's ballots set is cleared and then they're
	 * removed from the candidates list. At the end of the function a String is added to the results list denoting the current round,
	 * name of the eliminated candidate and the amount of #1's they had at the time.
	 */
	public void eliminateCandidate(int eliminate) {
		String loserName = candidates.get(eliminate).getName();
		int ones = candidates.get(eliminate).getBallots().size();
		eliminated.add(candidates.get(eliminate));

		for(Ballot b : candidates.get(eliminate).getBallots()) {
			while(getCandidateByID(eliminated,b.getFirstChoice()) != null) {
				b.eliminateCandidate(b.getFirstChoice());
			}
			getCandidateByID(candidates,b.getFirstChoice()).getBallots().add(b);
		}
		candidates.get(eliminate).getBallots().clear();
		candidates.remove(eliminate);

		results.add("Round " + round + ": " + loserName + " was eliminated with " + ones + " #1's");
	}

	/**
	 * Returns the position in the candidates list of the candidate who has the least #1's. Starts by declaring an integer 
	 * eliminate to store the position of the candidate to eliminate, then adds the candidates to a list of candidates with 
	 * the least votes. The for loop goes through all the candidates and searches for the candidate with the lowest amount 
	 * of #1s. In the first if-statement the eliminate integer is set to the newly found minimum position, and the leastVotes
	 * list in emptied since they are no longer the candidates with the lowest amount of #1s while the candidate at the new 
	 * position is added to the leastVotes list. If the second if-statement checks true then the candidate at the checked
	 * position is added to the leastVotes list. After the for loop, if there are more than one candidate in the leastVotes 
	 * list, then the tieBreaker method is called to decide who will be eliminated from those in the leastVotes list.
	 */
	public int getTrailingCandidate() {
		int eliminate = 0;
		leastVotes.add(candidates.get(eliminate));
		for(int i = 1;i < candidates.size();i++) { 
			if(candidates.get(i).getBallots().size() < candidates.get(eliminate).getBallots().size()) {
				eliminate = i;
				leastVotes.clear();
				leastVotes.add(candidates.get(i));
			}else if (candidates.get(i).getBallots().size() == candidates.get(eliminate).getBallots().size()) 
				leastVotes.add(candidates.get(i));
		}
		if(leastVotes.size() > 1) eliminate = candidates.firstIndex(getCandidateByID(candidates, tieBreaker()));
		leastVotes.clear();
		return eliminate;
	}

	/**
	 * Returns the ID of the candidate to be eliminated from a tie. Starts by initializing a variable "check",
	 * to store current rank the method is checking for, and an integer eliminate to store the position of the 
	 * candidate with the lowest #1's in the leastVotes list. The method then loops through the leastVotes list.
	 * If the count of paramenter rank of the current candidate is less than the count of the candidate in the 
	 * eliminate position, then the candidate at the eliminate position is removed and the eliminate is set to 
	 * the current position minus one, since the size of the list went down. If the current candidate's count is
	 * greater than the count of the candidate in the eliminate position, then the current candidate is removed 
	 * and if the current candidate was in a position lower than the elimination position, then the elimination
	 * position is decremented since the candidate in the elimination position went down in the list. This will 
	 * repeat until there is only one candidate in the leastVotes list or the check surpasses the count of the
	 * candidates. The method will return the candidate in the last position if there is still a tie, or return the
	 * candidate in the eliminate position if the tie has been settled.
	 * @return
	 */

	public int tieBreaker() {
		int check = 2;
		int eliminate = 0;

		while(leastVotes.size() > 1 && check <= numCandidates) {
			for(int i = 0;i<leastVotes.size();i++) {		
				if(getCount(leastVotes.get(i), check) < getCount(leastVotes.get(eliminate), check)) {
					leastVotes.remove(eliminate);
					eliminate = i - 1; 
				}else if(getCount(leastVotes.get(i), check) > getCount(leastVotes.get(eliminate), check)) {
					leastVotes.remove(i);
					if(i<eliminate) {
						eliminate--;
					}
				}
			}
			check++;
		}
		if(leastVotes.size()>1) return leastVotes.get(leastVotes.size() - 1).getID();
		return leastVotes.get(eliminate).getID();
	}

	/**
	 * Returns the count of the parameter candidate with parameter rank in the candidates list.
	 * Initializes a count and loops through each candidate's set and increments the count
	 * every time you find that the candidate with the parameter rank in a ballot has the
	 * same ID as the parameter candidate's ID.
	 */
	public int getCount(Candidate candidate, int rank) {
		int count = 0;
		for(int i = 0;i<candidates.size();i++) {
			for(Ballot b : candidates.get(i).getBallots()) {
				if(b.getRankByCandidate(candidate.getID()) == rank) count++;
			}
		}
		return count;
	}

	/** 
	 * Initializes a FileWriter object for the text file "results.txt", then loops 
	 * through the strings in the parameter list and writes them to the text file.
	 */
	public static void writeResults(List<String> list) throws IOException {
		FileWriter output = new FileWriter("results.txt");
		for(String s : list) {
			output.write(s+"\n");
		}
		output.close();
	}


	/** Non-member methods */

	/** Loops through a parameter list and returns the candidate whose ID matches the parameter ID. */
	public static Candidate getCandidateByID(List<Candidate> list, int ID) {
		for(Candidate c : list) {
			if(c.getID() == ID) {
				return c;
			}
		}
		return null;
	}

}
