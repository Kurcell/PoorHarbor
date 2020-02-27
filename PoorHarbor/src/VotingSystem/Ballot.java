package VotingSystem;

import Lists.List;
import Sets.Set;

public class Ballot {
	
	private int ballot;
	private List<Integer> votes; // Should be changed to a list instead of arr, decide when you understand the problem better
	
	
	public Ballot(String str) {
		ballot = -1; // placeholder
		for(int i = 0;i<2;i++) { // placeholder, will add elements into linked list according to ID
			
		}
		votes = null; // placeholder
		
	}
	
	public int getBallotNum() {
		return ballot;
	}
	
	public int getFirstChoice() { // returns ID of candidate with rank 1 in ballot
		for(int i = 0;i<this.votes.size();i++) {
			if(this.votes.get(i)==1) {
				return i+1;
			}
		}
		return -1;
	}
	
	public boolean eliminateCandidate(int ID) { // eliminates a candidate
		return false;
	}
	
	public boolean isValid() { // Checks if a ballot is valid or not
		return false;
	}
	
	public boolean isBlank() {
		for(int i = 0;i<this.votes.size();i++) {
			if(this.votes.get(i) != 0) {
				return false;
			}
		}
		return true;
	}
	

}
