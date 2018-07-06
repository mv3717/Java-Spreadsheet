package common.api.value;

/**
 * A value for tabular cells that are currently invalid.
 * 
 */
public final class InvalidValue implements Value {

  private final String expression;

  public InvalidValue(String expression) {
    this.expression = expression;
  }

  @Override
  public void evaluate(ValueEvaluator evaluator) {
    evaluator.evaluateInvalid(expression);
  }

  public String toString() {
    return "{" + expression + "}";
  }

}
