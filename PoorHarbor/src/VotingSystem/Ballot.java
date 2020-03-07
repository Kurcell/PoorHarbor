package VotingSystem;

import Lists.ArrayList;

import Lists.List;
import Sets.DynamicSet;
import Sets.Set;
import VotingSystem.Candidate;

public class Ballot {

	public class Vote { // Vote class used to store information of a single vote
		private int ID;
		private int rank;

		public Vote(String str) {
			String[] data = str.split(":");
			ID = Integer.parseInt(data[0]);
			rank = Integer.parseInt(data[1]);
		}
		public int getRank() {
			return this.rank;
		}
		public int getID() {
			return this.ID;
		}
	}

	private int ballot;
	private List<Vote> votes = new ArrayList<Vote>(1); // Should be changed to a list instead of arr, decide when you understand the problem better
	private List<Candidate> candidates = new ArrayList<Candidate>(1);

	public Ballot(String str) {
		String[] data = str.split(",");
		ballot = Integer.parseInt(data[0]);
		for(int i = 1;i<data.length;i++) { 
			Vote newVote = new Vote(data[i]);
			votes.add(newVote);
		}
	}

	public int getBallotNum() { // Return the ballot number
		return ballot;
	}

	public int getFirstChoice() { // Returns ID of candidate with rank 1 in ballot
		return getCandidateByRank(1);
	}

	public int getRankByCandidate(int candidateID) { // Returns the rank of the given candidate
		for(int i = 0;i<this.votes.size();i++) {
			if(votes.get(i).getID() == candidateID) {
				return votes.get(i).getRank();
			}
		}
		return -1;
	}

	public int getCandidateByRank(int rank) { // Returns the ID of the candidate with the given rank
		for(int i = 0;i<this.votes.size();i++) {
			if(votes.get(i).getRank() == rank) {
				return votes.get(i).getID();
			}
		}
		return -1;
	}

	public void sortBallot() {
		int ID = this.getFirstChoice();

	}

	public boolean eliminateCandidate(int ID) { // Eliminates a candidate
		return false;
	}

	public boolean isValid() { // Checks if a ballot is valid or not
		for(int i = 0;i<this.votes.size();i++) {
			for(int j = i+1;j<this.votes.size();j++) {
				if (this.votes.get(i).rank == this.votes.get(j).rank
						|| this.votes.get(i).ID == this.votes.get(j).ID){
					return false;
				}
			}
		}
		return true;
	}

	public boolean isBlank() { // Checks if the ballot is blank
		for(int i = 0;i<this.votes.size();i++) {
			if(this.votes.get(i).getRank() != 0) {
				return false;
			}
		}
		return true;
	}


	public void printBallot() { // Prints the ballot to the console for testing purposes
		System.out.println("Ballot: " + this.getBallotNum());
		for(int i = 0;i<this.votes.size();i++) {
			System.out.println(this.votes.get(i).getRank() + ":" + this.votes.get(i).getID());
		}
	}

}
