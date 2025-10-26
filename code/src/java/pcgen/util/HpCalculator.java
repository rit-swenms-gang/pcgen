package pcgen.util;

public class HpCalculator {
	private int con;
	public int getCon() { return con; }
	private int maxHp;
	public int getMaxHp() { return maxHp; }
	private int minHp;
	public int getMinHp() { return minHp; }

	private int hitDie;
	public int getHitDie() { return hitDie; }

	private final DiceRoller dice;

	/**
	 * 
	 * @param dice an object implementing DiceRoller interface for mock testing.
	 * @param dieNum the number of sides to the die.
	 * @param con the constitution modifier; the number to be added for every level to a character's HP.
	 */
	public HpCalculator(DiceRoller dice, int dieNum, int con) {
		this.dice = dice;
		this.hitDie = dieNum;
		this.con = con;

		this.maxHp = Math.max(dieNum + con, 1);
		this.minHp = Math.max(1 + con, 1);
	}

	public int rollHp(int level) {
		int hp = hitDie + con;
		for(int l = 2; l <= level; l++) {
			int roll = dice.roll(hitDie);
			hp += roll + con;
		}
		return hp;
	}
}
