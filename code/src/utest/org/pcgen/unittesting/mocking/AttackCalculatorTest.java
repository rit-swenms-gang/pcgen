package org.pcgen.unittesting.mocking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import pcgen.util.AttackCalculator;
import pcgen.util.DiceRoller;

class AttackCalculatorTest {

    @Test
    void hitWhenTotalMeetsAC() {
        DiceRoller dice = mock(DiceRoller.class);
        when(dice.d20()).thenReturn(12);               // control randomness

        AttackCalculator calc = new AttackCalculator(dice);
        assertEquals(AttackCalculator.Outcome.HIT, calc.attack(8, 20)); // 12 + 8 = 20

        verify(dice).d20(); // behavior verification
    }

    @Test
    void missWhenBelowAC() {
        DiceRoller dice = mock(DiceRoller.class);
        when(dice.d20()).thenReturn(5);

        AttackCalculator calc = new AttackCalculator(dice);
        assertEquals(AttackCalculator.Outcome.MISS, calc.attack(3, 12)); // 5 + 3 = 8 < 12
    }

    @Test
    void critOnNatural20() {
        DiceRoller dice = mock(DiceRoller.class);
        when(dice.d20()).thenReturn(20);

        AttackCalculator calc = new AttackCalculator(dice);
        assertEquals(AttackCalculator.Outcome.CRIT, calc.attack(0, 100)); // nat 20 auto-crit
    }
}