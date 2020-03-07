package Testing;

import VotingSystem.Ballot;

public class test {
	
	public static void main(String[] args) {
		Ballot ball = new Ballot("1234,5:1,2:2,4:3,1:4,3:5");
		
		ball.printBallot();
		System.out.println(ball.getBallotNum());
		System.out.println(ball.getFirstChoice());
		System.out.println(ball.getRankByCandidate(4));
		if(!ball.isBlank()) {
			System.out.println("bang");
		}
		
		
	}

}
