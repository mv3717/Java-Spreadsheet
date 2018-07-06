package common.api.value;

/**
 * A value for tabular cells that represented an expression that evaluated to a
 * double.
 * 
 */
public final class DoubleValue implements Value {

  private final double value;

  public DoubleValue(double d) {
    this.value = d;
  }

  @Override
  public void evaluate(ValueEvaluator evaluator) {
    evaluator.evaluateDouble(value);
  }

  @Override
  public String toString() {
    return Double.toString(value);
  }

}
