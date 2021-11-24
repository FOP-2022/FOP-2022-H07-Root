package H07.predicate;

import java.util.function.DoublePredicate;

public class EpsilonEnvironmentPred implements DoublePredicate {

  private final double eplison;
  private final double x0;

  /**
   * Constructs the object
   *
   * @param x0      the target value
   * @param eplison the acceptable difference
   */
  public EpsilonEnvironmentPred(double x0, double eplison) {
    this.x0 = x0;
    this.eplison = Math.max(0, eplison);
  }

  /**
   * Test if the value is close enough to the target
   *
   * @param value the value to test
   * @return true if the absolute difference is less than epsilon
   */
  @Override
  public boolean test(double value) {
    return Math.abs(value - x0) <= eplison;
  }
}
