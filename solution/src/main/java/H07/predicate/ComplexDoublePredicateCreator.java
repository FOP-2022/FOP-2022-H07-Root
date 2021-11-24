package H07.predicate;

import java.util.function.DoublePredicate;

public class ComplexDoublePredicateCreator {

  /**
   * Combines the predicates by OR over each subarray and by AND over the resulting one.
   *
   * @param predicates the predicates as a not null and non-empty array
   * @return a predicate containing the aforementioned Conjunction over Disjunctions
   */
  public static DoublePredicate buildComplexPredicate(DoublePredicate[][] predicates) {
    return buildComplexPredicate(buildDisjunction(predicates[0], true), predicates, 1);
  }

  /**
   * Combines the predicates with OR
   *
   * @param predicates the predicate
   * @param forward    if false combines them in opposite iteration direction
   * @return the Disjunction over predicates
   */
  private static DoublePredicate buildDisjunction(DoublePredicate[] predicates, boolean forward) {
    DoublePredicate result;
    if (forward) {
      result = predicates[0];
      for (int i = 1; i < predicates.length; i++) {
        result = result.or(predicates[i]);
      }
    } else {
      result = predicates[predicates.length - 1];
      for (int i = predicates.length - 2; i >= 0; i--) {
        result = result.or(predicates[i]);
      }
    }
    return result;
  }

  /**
   * @param acc        collect the already build disjunctions with AND, i.e. into a conjunction
   * @param predicates the predicates
   * @param i          recursive counter
   * @return one predicate after the last index was processed
   */
  private static DoublePredicate buildComplexPredicate(DoublePredicate acc, DoublePredicate[][] predicates, int i) {
    if (i >= predicates.length) {
      return acc;
    }
    return buildComplexPredicate(acc.and(buildDisjunction(predicates[i], i % 2 == 0)), predicates, i + 1);
  }

  /**
   * This predicate requires the double to be:
   * d >= -20 * Math.PI && d <= 10 * Math.E OR Math.sin(d) > Math.cos(d) OR d < Math.pow(Math.log(d), 3)
   * AND
   * not too far from n + 0.5 for some n, that is a natural number in [1, 1000]
   *
   * @return the predicate
   */
  public static DoublePredicate getDefaultComplexPredicate() {
    DoublePredicate[][] predicates = new DoublePredicate[3][];
    final var LARGE_ARRAY_SIZE = 1000;
    predicates[0] = new DoublePredicate[LARGE_ARRAY_SIZE];
    predicates[1] = new DoublePredicate[LARGE_ARRAY_SIZE];
    for (int i = 0; i < LARGE_ARRAY_SIZE; i++) {
      predicates[0][i] = new EpsilonEnvironmentPred(i + 0.5, i / 50000.0);
      int backwardsI = i + 1;
      predicates[1][LARGE_ARRAY_SIZE - backwardsI] = d -> Math.abs(1000 - backwardsI + 0.5 - d) < backwardsI / 50000.0;
    }
    predicates[2] = new DoublePredicate[]{
      (double d) -> {
        return d >= -20 * Math.PI && d <= 10 * Math.E;
      },
      (double d) -> {
        return Math.sin(d) > Math.cos(d);
      },
      (double d) -> {
        return d < Math.pow(Math.log(d), 3);
      },
    };
    return buildComplexPredicate(predicates);
  }
}
