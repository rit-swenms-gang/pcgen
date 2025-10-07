package pcgen.util;

import java.util.Random;

public class DefaultDiceRoller implements DiceRoller {
    private final Random rng;

    public DefaultDiceRoller() {
        this(new Random());
    }

    public DefaultDiceRoller(Random rng) {
        this.rng = rng;
    }

    @Override
    public int roll(int sides) {
        if (sides <= 0) {
            throw new IllegalArgumentException("sides must be > 0");
        }
        // nextInt(n) -> [0, n), so add 1 to make it [1, n]
        return rng.nextInt(sides) + 1;
    }
}