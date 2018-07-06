package common.api.value;

/**
 * Interface representing the values that a particular tabular cell can take.
 */
public interface Value {

  void evaluate(ValueEvaluator evaluator);

}
