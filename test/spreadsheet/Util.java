package spreadsheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import common.api.value.Value;
import common.api.value.ValueEvaluator;

public class Util {

  public static void assertIsString(Value value, final String string) {
    value.evaluate(new ValueEvaluator() {

      @Override
      public void evaluateString(String expression) {
        assertEquals("assertIsString: got String!", string, expression);
      }

      @Override
      public void evaluateLoop() {
        fail("assertIsString: got Loop! (" + string + ")");
      }

      @Override
      public void evaluateInvalid(String expression) {
        assertEquals("assertIsString: got Invalid!", string, expression);
      }

      @Override
      public void evaluateDouble(double value) {
        assertEquals("assertIsString: got Double!", string, value);
      }
    });
  }

  public static void assertIsDouble(Value value, final double d) {
    value.evaluate(new ValueEvaluator() {

      @Override
      public void evaluateString(String expression) {
        assertEquals("assertIsDouble: got String!", d, expression);
      }

      @Override
      public void evaluateLoop() {
        fail("assertIsDouble: got Loop! (" + d + ")");
      }

      @Override
      public void evaluateInvalid(String expression) {
        assertEquals("assertIsDouble: got Invalid!", d, expression);
      }

      @Override
      public void evaluateDouble(double value) {
        assertEquals("assertIsDouble: got Double!", d, value, 0);
      }
    });
  }

  public static void assertIsLoopValue(Value value) {
    value.evaluate(new ValueEvaluator() {

      @Override
      public void evaluateDouble(double value) {
        fail("assertIsLoopValue: got double" + value);
      }

      @Override
      public void evaluateInvalid(String expression) {
        fail("assertIsLoopValue: got InValid" + expression);
      }

      @Override
      public void evaluateLoop() {
        // yay!
      }

      @Override
      public void evaluateString(String expression) {
        fail("assertIsLoopValue: got String" + expression);

      }
    });
  }

  public static void assertIsInvalidValue(Value value,
      final String expectedInvalidValue) {
    value.evaluate(new ValueEvaluator() {

      @Override
      public void evaluateDouble(double value) {
        assertEquals("assertIsInvalidValue got double", expectedInvalidValue,
            value);
      }

      @Override
      public void evaluateInvalid(String expression) {
        assertEquals("assertIsInvalidValue", expectedInvalidValue, expression);
      }

      @Override
      public void evaluateLoop() {
        assertEquals("assertIsInvalidValue got Loop", expectedInvalidValue,
            "#LOOP");
      }

      @Override
      public void evaluateString(String expression) {
        assertEquals("assertIsInvalidValue got String", expectedInvalidValue,
            expression);
      }
    });
  }
}
