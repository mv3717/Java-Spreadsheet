package common.api.value;

/**
 * A value for tabular cells that are part of a loop.
 */
public final class LoopValue implements Value {

  public static final LoopValue INSTANCE = new LoopValue();

  private LoopValue() {
    // not visible
  }

  @Override
  public void evaluate(ValueEvaluator evaluator) {
    evaluator.evaluateLoop();
  }

  @Override
  public String toString() {
    return "#LOOP";
  }
}
