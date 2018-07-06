package common.api.value;

/**
 * Evaluator interface for values.
 */
public interface ValueEvaluator {

  void evaluateDouble(double value);

  void evaluateLoop();

  void evaluateString(String expression);

  void evaluateInvalid(String expression);
}
