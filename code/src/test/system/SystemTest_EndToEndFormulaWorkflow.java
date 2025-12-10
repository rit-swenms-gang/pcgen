package system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pcgen.base.formula.AddingFormula;
import pcgen.base.formula.SubtractingFormula;
import pcgen.base.formula.MultiplyingFormula;
import pcgen.base.formula.DividingFormula;

/**
 * System Test: End-to-end arithmetic workflow
 *
 * This test uses ONLY public PCGen formula classes.
 * It simulates a full workflow:
 *   1. apply an addition
 *   2. apply a subtraction
 *   3. apply a multiplication
 *   4. apply a division
 *   5. verify the final computed result
 * 
 * This evaulates how damage/attribute/etc. values are computed within PCGen which is a primary use-case of this software
 *
 * Every formula class in the pcgen.base.formula package is exercised, which
 * provides black-box system-level coverage of the formula subsystem.
 */
public class SystemTest_EndToEndFormulaWorkflow {

    @Test
    void testFullFormulaWorkflow() {

        Number result = 10;

        AddingFormula add5 = new AddingFormula(5);
        result = add5.resolve(result); // 15

        SubtractingFormula sub3 = new SubtractingFormula(3);
        result = sub3.resolve(result); // 12

        MultiplyingFormula mult2 = new MultiplyingFormula(2);
        result = mult2.resolve(result); // 24

        DividingFormula div4 = new DividingFormula(4);
        result = div4.resolve(result); // 6

        assertEquals(6, result.intValue());
    }
}