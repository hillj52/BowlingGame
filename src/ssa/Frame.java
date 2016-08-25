package ssa;

import java.util.Random;

public class Frame {
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
