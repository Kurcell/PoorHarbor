package VotingSystem;

import Sets.DynamicSet;
import Sets.Set;

public class Candidate {
	/** Candidate contains three private fields:
	 * A string to store it's name, an integer that stores it's ID, and a set of ballots
	 * that stores all the ballots in which the candidate's rank is 1. Filling the ballots set
	 * is handled in the Election class.
	 */
	private String name;
	private int ID;
	private Set<Ballot> ballots = new DynamicSet<Ballot>(1);
	
	/** The constructor takes a string in format "candidateName,candidateId" and splits it
	 * using String's split method and sets them to the candidate accordingly.
	 */
	public Candidate(String str) {
		String[] data = str.split(",");
		name = data[0];
		ID = Integer.parseInt(data[1]);
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return ID;
	}
	
	public Set<Ballot> getBallots(){
		return ballots;
	}
	
}
