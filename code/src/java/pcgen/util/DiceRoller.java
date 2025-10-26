package pcgen.util;

/** Minimal seam to isolate randomness for unit tests. */
public interface DiceRoller {
    /** Returns an integer in [1, sides]. */
    int roll(int sides);

    /** Convenience: d20 roll. */
    default int d20() {
        return roll(20);
    }
}