package pcgen.base.calculation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pcgen.base.formula.base.DependencyManager;
import pcgen.base.formula.base.EvaluationManager;
import pcgen.base.formula.base.FormulaSemantics;

/**
 * Integration test that uses NEPCalculation + EvaluationManager + TypedKey
 * without touching FormatManager or CalculationModifier.
 */
public class IntegrationSmokeTest
{
    /**
     * Simple NEPCalculation that reads the INPUT TypedKey and multiplies it by 2.
     */
    private static class DoubleInputCalc implements NEPCalculation<Double>
    {
        @Override
        public Double process(EvaluationManager mgr)
        {
            Object input = mgr.get(EvaluationManager.INPUT);
            double v = (double) input;
            return v * 2;
        }

        @Override
        public void getDependencies(DependencyManager mgr)
        {
            // no dependencies for this simple test
        }

        @Override
        public String getInstructions()
        {
            return "DOUBLE";
        }

        @Override
        public String getIdentification()
        {
            return "DoubleInputCalc";
        }

        @Override
        public int getInherentPriority()
        {
            return 0;
        }

        @Override
        public void isValid(FormulaSemantics sem)
        {
            // always valid
        }
    }

    @Test
    public void testDoubleInputCalculation()
    {
        DoubleInputCalc calc = new DoubleInputCalc();

        // Create manager and store 10.0 in the INPUT TypedKey
        EvaluationManager mgr = new EvaluationManager()
                .getWith(EvaluationManager.INPUT, 10.0);

        double result = calc.process(mgr);

        assertEquals(20.0, result, 0.001);
    }
}