package ssa;

import java.util.Random;

public class SimulatedBowling {

	private Frame[][] bowlingSeries;
	private int[][] frameScores;
	private Random rn;
	
	public void bowl() {
		this.createScores();
		this.populateScores();
		System.out.println(this);
	}
	
	private void populateScores() {
		Frame currFrame;
		for (int i=0;i<3;i++) {
			for (int j=0;j<10;j++) {
				currFrame = bowlingSeries[i][j];
				if (currFrame.isStrike()) {
					frameScores[i][j] = 15;
				} else if (currFrame.isSpare()) {
					frameScores[i][j] = 12;
				} else {
					frameScores[i][j] = currFrame.getFirstBall() + currFrame.getSecondBall();
				}
			}
		}
	}
	
	private void createScores() {
		Frame currFrame = null;
		for (int i=0;i<3;i++) {
			for (int j=0;j<10;j++) {
				currFrame = bowlingSeries[i][j];
				currFrame.bowlOneBall();
				if (currFrame.bowlAgain()) {
					currFrame.bowlOneBall();
				}
				if(currFrame.getIsTenth()) {
					currFrame.calcTenthBonus();
				}
			}
		}
	}
	
	public String toString() {
		int total;
		String sb = "Frames	 1   2   3   4   5   6   7   8   9   10   Total\n";
		for (int i=0;i<3;i++) {
			total = 0;
			sb += "Game " + (i+1) + "  ";
			for (int j=0;j<10;j++) {
				sb += bowlingSeries[i][j] + "|";
			}
			sb += "\n        ";
			for (int j=0;j<10;j++) {
				total += frameScores[i][j];
				sb += frameScores[i][j] + "";
				if (j==9) {
					sb += "  ";
				}
				if (frameScores[i][j] >= 100) {
					sb += "|";
				} else if (frameScores[i][j] >= 10) {
					sb += " |";
				} else {
					sb += "  |";
				}
			}
			sb += " " + total + "\n";
		}
		return sb;
	}
	public SimulatedBowling() {
		bowlingSeries = new Frame[3][10];
		frameScores = new int[3][10];
		rn = new Random();
		for (int i=0;i<3;i++) {
			for (int j=0;j<10;j++) {
				if (j<9) {
					bowlingSeries[i][j] = new Frame(false,rn);
				} else {
					bowlingSeries[i][j] = new Frame(true,rn);
				}
			}
		}
	}
}

