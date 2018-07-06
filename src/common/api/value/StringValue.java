package common.api.value;

/**
 * A value for a tabular cell that contains an uninterpreted String.
 */
public final class StringValue implements Value {

  private final String expression;

  public StringValue(String expression) {
    this.expression = expression;
  }

  @Override
  public void evaluate(ValueEvaluator evaluator) {
    evaluator.evaluateString(expression);
  }

  @Override
  public String toString() {
    return expression;
  }
}
