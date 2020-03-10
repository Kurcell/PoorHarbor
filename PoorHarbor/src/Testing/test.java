package Testing;

import VotingSystem.Ballot;

public class test {
	
	public static void main(String[] args) {
		Ballot ball = new Ballot("632,5:2,2:1,4:3,3:4,1:5");
		ball.printBallot(1);
		ball.sortBallot();
		ball.printBallot(1);
		System.out.println(ball.getBallotNum());
		System.out.println(ball.getFirstChoice());
		System.out.println(ball.getRankByCandidate(4));
		if(!ball.isBlank()) {
			System.out.println("bang");
		}
		
		
	}

}
