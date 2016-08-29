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
					frameScores[i][j] = 10;
					if (currFrame.getIsTenth()) {
						frameScores[i][j] += currFrame.getTotalTenthBonus();
					} else if (j==8 && bowlingSeries[i][j+1].isStrike()){
						frameScores[i][j] += 10 + bowlingSeries[i][j+1].getFirstBonus();
					} else {
						if (bowlingSeries[i][j+1].isStrike()) {
							frameScores[i][j] += 10;
							frameScores[i][j] += bowlingSeries[i][j+2].getFirstBall();
						} else {
							frameScores[i][j] += bowlingSeries[i][j+1].get2BallTotal();
						}
					}
				} else if (currFrame.isSpare()) {
					frameScores[i][j] = 10;
					if(currFrame.getIsTenth()) {
						frameScores[i][j] += currFrame.getFirstBonus();
					} else {
						frameScores[i][j] += bowlingSeries[i][j+1].getFirstBall();
					}
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
		int total = 0;
		int totalSeries = 0;
		String sb = "Frames	 1   2   3   4   5   6   7   8   9   10   Total\n\n";
		for (int i=0;i<3;i++) {
			total = 0;
			sb += "Game " + (i+1) + "  ";
			for (int j=0;j<10;j++) {
				sb += bowlingSeries[i][j] + "|";
			}
			sb += "\n        ";
			for (int j=0;j<10;j++) {
				total += frameScores[i][j];
				sb += total + "";
				if (j==9) {
					sb += "  ";
				}
				if (total >= 100) {
					sb += "|";
				} else if (total >= 10) {
					sb += " |";
				} else {
					sb += "  |";
				}
			}
			sb += " " + total + "\n";
			totalSeries += total;
		}
		sb += "\nTotal Series                                       " + totalSeries + "\n";
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

class Frame {
	private int[] pinsDowned;
	private int ballsThrown;
	private boolean isTenth;
	private int[] tenthBonus;
	private Random rn;
	
	public boolean getIsTenth() {
		return this.isTenth;
	}
	
	public int getFirstBall() {
		return pinsDowned[0];
	}
	
	public int getSecondBall() {
		return pinsDowned[1];
	}
	
	public int pinsLeft() {
		return 10 - pinsDowned[0];
	}
	
	public boolean isStrike() {
		return this.getFirstBall() == 10;
	}
	
	public boolean isSpare() {
		return (this.getFirstBall() + this.getSecondBall()) == 10;
	}
	
	public boolean isStrikeOrSpare() {
		return this.isStrike() || this.isSpare();
	}
	
	public boolean bowlAgain() {
		if (this.isStrike() || ballsThrown >= 2) {return false;}
		else {return true;}
	}
	
	public int get2BallTotal() {
		return this.getFirstBall() + this.getSecondBall();
	}
	
	public void calcTenthBonus() {
		if (this.isStrike()) {
			this.tenthBonus[0] = getPinsDowned();
			if (tenthBonus[0] == 10) {tenthBonus[1] = getPinsDowned();}
			else {
				int extraPins = getPinsDowned();
				if (extraPins > (10 - tenthBonus[0])) {tenthBonus[1] += (10-tenthBonus[0]);}
				else {tenthBonus[1] += extraPins;}
			}
		} else if (this.isSpare()) {
			this.tenthBonus[0] = getPinsDowned();
		} else {
			this.tenthBonus[0] = 0;
		}
	}
	
	public int getTotalTenthBonus() {
		return this.tenthBonus[0] + this.tenthBonus[1];
	}
	
	public int getFirstBonus() {
		return this.tenthBonus[0];
	}
	
	public void bowlOneBall() {
		if (this.ballsThrown == 0) {
			pinsDowned[0] = this.getPinsDowned();
			ballsThrown++;
		} else {
			ballsThrown++;
			int pins = getPinsDowned();
			if (pins > this.pinsLeft()) {
				pinsDowned[1] = this.pinsLeft();
			} else {
				pinsDowned[1] = pins;
			}
		}
	}
	
	private int getPinsDowned() {
		return rn.nextInt(11);
	}
	
	private String printHelper() {
		if(this.isStrike()) {
			return "X  ";
		} else if (this.isSpare()) {
			return this.getFirstBall() + " /";
		} else {
			return this.getFirstBall() + " " + this.getSecondBall();
		}
	}
	
	private String printHelperTenth() {
		String sb = "";
		if(this.isStrike()) {
			sb += "X ";
			if (tenthBonus[0] == 10) {
				sb += "X ";
			} else {
				sb += tenthBonus[0] + " ";
			}
			if (tenthBonus[1] == 10) {
				sb += "X";
			} else if ((tenthBonus[0] + tenthBonus[1]) == 10){
				sb += "/";
			} else {
				sb += "" + tenthBonus[1];
			}
		} else if (this.isSpare()) {
			sb += this.getFirstBall() + " /";
			if (tenthBonus[0] == 10) {
				sb += " X";
			} else {
				sb += " " + tenthBonus[0];
			}
		} else {
			return this.printHelper() + "  ";
		}
		return sb;
	}
	
	public String toString() {
		String sb = "";
		if (!this.isTenth) {
			sb += this.printHelper();
		} else {
			sb += this.printHelperTenth();
		}
		return sb;
	}
	
	public Frame(boolean isTenth, Random rn) {
		this.isTenth = isTenth;
		this.pinsDowned = new int[] {0,0};
		this.ballsThrown = 0;
		this.tenthBonus = new int[] {0,0};
		this.rn = rn;
	}
}
