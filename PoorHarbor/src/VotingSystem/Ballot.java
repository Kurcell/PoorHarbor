package VotingSystem;

import Lists.ArrayList;


import Lists.List;
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
		public void setRank(int rank) {
			this.rank = rank;
		}
		public void setID(int ID) {
			this.ID = ID;
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
		
		for(int i = 0;i < this.votes.size(); i++) {
			Vote temp = new Vote(this.votes.get(i).ID + ":" + this.votes.get(i).rank);;
		    int j = i - 1;
		    while( j >= 0 && this.votes.get(j).rank > temp.rank) {
		    	this.votes.get(j+1).setID(this.votes.get(j).getID());
		    	this.votes.get(j+1).setRank(this.votes.get(j).getRank());
		    	j--;
		    }
		    this.votes.get(j+1).setRank(temp.rank);
		    this.votes.get(j+1).setID(temp.ID);
		}
	}

	public boolean eliminateCandidate(int ID) { // Eliminates a candidate
		int removedRank = 0;
		for(Vote vote : this.votes) {
			if (vote.ID == ID) {
				removedRank = vote.rank;
				votes.remove(vote);
			}
		}
		for(Vote vote : this.votes) {
			if (vote.rank > removedRank) {
				vote.rank--;
			}
		}
		return true;
	}

	public boolean isValid() { // Checks if a ballot is valid or not
		this.sortBallot();
		
		if(!this.isBlank() &&this.getFirstChoice() == -1) return false;
		
		for(int i = 0;i<this.votes.size();i++) {
			if(i<this.votes.size()-1 && this.votes.get(i).rank+1 != this.votes.get(i+1).rank) {
				return false;
			}
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


	public void printBallot(int num) { // Prints the ballot to the console for testing purposes
		System.out.println("Ballot: " + this.getBallotNum());
		if(num == 1) {
		for(int i = 0;i<this.votes.size();i++) {
			System.out.println(this.votes.get(i).getRank() + ":" + this.votes.get(i).getID());
		}}
	}

}
