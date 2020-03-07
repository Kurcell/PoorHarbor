package VotingSystem;

import Sets.DynamicSet;
import Sets.Set;

public class Candidate {
	private String name;
	private int ID;
	private Set<Ballot> ballots = new DynamicSet<Ballot>(1);
	
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
	
	public void printCandidate() {
		System.out.println(this.ID + this.name);
	}
}