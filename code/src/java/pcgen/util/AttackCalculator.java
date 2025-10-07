package pcgen.util;

/** Tiny example consumer of DiceRoller to make mocking meaningful. */
public class AttackCalculator {

    public enum Outcome { MISS, HIT, CRIT }

    private final DiceRoller dice;

    public AttackCalculator(DiceRoller dice) {
        this.dice = dice;
    }

    /**
     * Roll a d20 and decide outcome:
     * - Natural 20 => CRIT
     * - Otherwise HIT if roll + attackBonus >= targetAC
     * - Else MISS
     */
    public Outcome attack(int attackBonus, int targetAC) {
        int roll = dice.d20();
        if (roll == 20) return Outcome.CRIT;
        int total = roll + attackBonus;
        return (total >= targetAC) ? Outcome.HIT : Outcome.MISS;
    }
}