package VotingSystem;

import Lists.ArrayList;


import Lists.List;
import VotingSystem.Candidate;

public class Ballot {

	/**
	 * A class was created for the votes so the ballots could have objects that contained a
	 * vote's candidate ID and the preference rank.
	 */
	public class Vote {
		private int ID;
		private int rank;
		
		/**
		 * The constructor takes in a string of format "ID:rank", then uses String's split method to 
		 * separate the ID and rank into different strings and sets them to the vote accordingly.
		 */
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
		public void setRank(int num) {
			this.rank = num;
		}
		public void setID(int num) {
			this.ID = num;
		}
	}
	/**
	 * The Ballot class utilizes two private fields:
	 * An integer "ballot", to store the ballot number, and an ArrayList "votes", to store the votes within the ballot.
	 */
	private int ballot;
	private List<Vote> votes = new ArrayList<Vote>(1); 

	/**
	 * The constructor takes a string of format "ballot#,vote,vote,..." and splits them into separate strings 
	 * using String's split method. Once split, it then sets the ballot number accordingly and loops through
	 * the rest of the split array and stores all the votes in the votes ArrayList.
	 */
	public Ballot(String str) {
		String[] data = str.split(",");
		ballot = Integer.parseInt(data[0]);
		for(int i = 1;i<data.length;i++) { 
			Vote newVote = new Vote(data[i]);
			votes.add(newVote);
		}
	}

	/** Returns the ballot number. */
	public int getBallotNum() { // Return the ballot number
		return ballot;
	}
	/** Uses the getCandidateByRank method to return the ID of the candidate with rank 1. */
	public int getFirstChoice() {
		return getCandidateByRank(1);
	}
	/** Loops through all the votes and returns the rank of the candidate with the given ID. */
	public int getRankByCandidate(int candidateID) {
		for(int i = 0;i<this.votes.size();i++) {
			if(votes.get(i).getID() == candidateID) {
				return votes.get(i).getRank();
			}
		}
		return -1;
	}
	/** Loops through all votes to find the candidate with the given rank. */
	public int getCandidateByRank(int rank) {
		for(int i = 0;i<this.votes.size();i++) {
			if(votes.get(i).getRank() == rank) {
				return votes.get(i).getID();
			}
		}
		return -1;
	}
	
	/**
	 * This method loops through the votes to find the candidate with the given ID and remove them
	 * for the ballot, while storing the rank they had before removal. Votes is looped through again
	 * to decrement the rank of any candidate whose rank was higher that the removed, to ensure that
	 * the ballot remains valid.
	 */
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
	
	/** 
	 * Loops through votes and if any vote contains a rank, then it returns false
	 * denoting that the ballot is not blank. However it may still be invalid.
	 */
	public boolean isBlank() { // Checks if the ballot is blank
		for(int i = 0;i<votes.size();i++) {
			if(votes.get(i).getRank() != 0) {
				return false;
			}
		}
		return true;
	}
	
	/** 
	 * Loops through the votes to search for the candidate with the highest ID.
	 * Used to facilitate verifying validity.
	 */
	public int getHighestID() {
		int max = 0;
		for(int i = 1;i<votes.size();i++) {
			if(votes.get(i).ID > votes.get(max).getID()) {
				max = i;
			}
		}
		return votes.get(max).ID;
	}
	
	/** 
	 * This method will check if a ballot is valid. It starts by checking if the highest id in the ballot is greater than 
	 * the highest candidate id available in the parameter list or if the ballot has no first choice, either results in an 
	 * invalid ballot. There's a double for-loop to check more conditions. The outer loop checks if the sorted ballot is 
	 * not contiguous, while the inner loop checks if there are repeated IDs or ranks, either also results in an invalid 
	 * ballot. If the method makes it to the end then it satisfies the requirements of a valid ballot.
	 */
	public boolean isValid(List<Candidate> candidates) {
		if(getHighestID() > candidates.get(candidates.size()-1).getID() || getFirstChoice() == -1) {
			return false;
		}

		for(int i = 0;i<votes.size();i++) {
			if(i<votes.size()-1 && votes.get(i).rank+1 != votes.get(i+1).rank) {
				return false;
			}
			for(int j = i+1;j<votes.size();j++) {
				if (votes.get(i).rank == votes.get(j).rank
						|| votes.get(i).ID == votes.get(j).ID){
					return false;
				}
			}
		}
		return true;
	}
	

	//	public void printBallot(int num) { // Prints the ballot to the console for testing purposes
	//		System.out.println("Ballot: " + getBallotNum());
	//		if(num == 1) {
	//		for(int i = 0;i<votes.size();i++) {
	//			System.out.println(votes.get(i).getRank() + ":" + votes.get(i).getID());
	//		}}
	//	}

}
