package org.pcgen.unittesting.mocking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import pcgen.util.DiceRoller;
import pcgen.util.HpCalculator;

public class HpCalculatorTest {
	/**
	 * TEST: Roll maximum possible HP for a 10th-level character
	 * with a d6 hit die.
	 */
	@Test
	void rollMaxHp() {
		DiceRoller dice = mock(DiceRoller.class);
		int die = 6;
		int con = 2;
		int level = 10;
		when(dice.roll(die)).thenReturn(die);

		HpCalculator calc = new HpCalculator(dice, die, con);
		assertEquals(calc.getMaxHp() * level, calc.rollHp(level));
	}

	@Test
	void rollMinHp() {
		DiceRoller dice = mock(DiceRoller.class);
		int die = 6;
		int con = 2;
		int level = 10;
		when(dice.roll(die)).thenReturn(1);

		HpCalculator calc = new HpCalculator(dice, die, con);
		assertEquals(calc.getMaxHp() + calc.getMinHp() * (level - 1), calc.rollHp(level));
	}

	/**
	 * Tests for "average" HP according to D&D 5e HP rules (mean die roll rounded up).
	 */
	@Test
	void rollAvgHp() {
		DiceRoller dice = mock(DiceRoller.class);
		int die = 6;
		int con = 2;
		int level = 10;

		int avg = Math.ceilDiv(die, 2);

		when(dice.roll(die)).thenReturn(avg);

		HpCalculator calc = new HpCalculator(dice, die, con);
		int expectedHp = die + (avg * (level - 1)) + (con * level);
		assertEquals(expectedHp, calc.rollHp(level));
	}
}
