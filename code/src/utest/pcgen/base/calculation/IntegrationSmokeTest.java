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

        // Create manager and store 11.0 in the INPUT TypedKey
        EvaluationManager mgr = new EvaluationManager()
                .getWith(EvaluationManager.INPUT, 11.0);

        double result = calc.process(mgr);

        assertEquals(22.0, result, 0.001);
    }

    /**
     * Simple NEPCalculation that reads the Input TypedKey and raises it to the power of 2.
     */
    private static class SquareInputCalc implements NEPCalculation<Double>
    {
        @Override
        public Double process(EvaluationManager mgr)
        {
            Object input = mgr.get(EvaluationManager.INPUT);
            double v = (double) input;
            return Math.pow(v, 2);
        }

        @Override
        public void getDependencies(DependencyManager mgr)
        {
            // no dependencies for this simple test
        }

        @Override
        public String getInstructions()
        {
            return "SQUARE";
        }

        @Override
        public String getIdentification()
        {
            return "SquareInputCalc";
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
    public void TestSquareInputCalculation() {
        SquareInputCalc calc = new SquareInputCalc();

        EvaluationManager manager = new EvaluationManager().getWith(EvaluationManager.INPUT, 11.0);

        double result = calc.process(manager);

        assertEquals(110.0, result, 0.001);
    }

    private static class HalfRoundUpInputCalc implements NEPCalculation<Double>
    {
        @Override
        public Double process(EvaluationManager mgr)
        {
            Object input = mgr.get(EvaluationManager.INPUT);
            double v = (double) input;
            return Math.ceil(v/2);
        }

        @Override
        public void getDependencies(DependencyManager mgr)
        {
            // no dependencies for this simple test
        }

        @Override
        public String getInstructions()
        {
            return "HALFROUNDUP";
        }

        @Override
        public String getIdentification()
        {
            return "HalfRoundUpInputCalc";
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
    public void testHalfRoundUpInputCalculation()
    {
        HalfRoundUpInputCalc calc = new HalfRoundUpInputCalc();

        EvaluationManager mgr = new EvaluationManager()
                .getWith(EvaluationManager.INPUT, 11.0);

        double result = calc.process(mgr);

        assertEquals(6.0, result, 0.001);
    }


//round down

private static class HalfRoundDownInputCalc implements NEPCalculation<Double>
    {
        @Override
        public Double process(EvaluationManager mgr)
        {
            Object input = mgr.get(EvaluationManager.INPUT);
            double v = (double) input;
            return Math.floor(v / 2);
        }

        @Override
        public void getDependencies(DependencyManager mgr)
        {
            // no dependencies for this simple test
        }

        @Override
        public String getInstructions()
        {
            return "HALFROUNDDOWN";
        }

        @Override
        public String getIdentification()
        {
            return "HalfRoundDownInputCalc";
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
    public void testHalfRoundDownInputCalculation()
    {
        HalfRoundDownInputCalc calc = new HalfRoundDownInputCalc();

        EvaluationManager mgr = new EvaluationManager()
                .getWith(EvaluationManager.INPUT, 11.0);

        double result = calc.process(mgr);

        assertEquals(5.0, result, 0.001);
    }
    
}