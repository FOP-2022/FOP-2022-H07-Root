package h07.predicate;

import java.util.function.DoublePredicate;

/**
 * A utility class with static methods to create a {@link DoublePredicate}.
 */
public class DoublePredicateFactory {

    /**
     * Combines the predicates by OR over each subarray and by AND over the resulting one.
     *
     * @param predicates the predicates as a not null and non-empty array
     * @return a predicate containing the aforementioned Conjunction over Disjunctions
     */
    public DoublePredicate buildComplexPredicate(DoublePredicate[][] predicates) {
        DoublePredicate[] disjunctions = new DoublePredicate[predicates.length];
        for (int i = 0; i < predicates.length; i++) {
            disjunctions[i] = buildDisjunction(predicates[i], i % 2 == 0);
        }
        return buildConjunction(disjunctions);
    }

    /**
     * Combines the predicates with a logical OR.
     *
     * @param predicates the predicates to combine
     * @param forward    if false combines them in opposite iteration direction
     * @return the Disjunction over predicates
     */
    public DoublePredicate buildDisjunction(DoublePredicate[] predicates, boolean forward) {
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
     * Combines the predicates with a logical AND.
     *
     * @param predicates the predicates to combine
     * @return the Conjunction over predicates
     */
    public DoublePredicate buildConjunction(DoublePredicate[] predicates) {
        System.out.println(">" + this);
        return buildConjunction(predicates[0], predicates, 1);
    }

    /**
     * Combines the predicates with a logical AND.
     *
     * @param acc        collects the already build conjunction
     * @param predicates the predicates to combine
     * @param i          recursive counter
     * @return one predicate after the last index was processed
     */
    public DoublePredicate buildConjunction(DoublePredicate acc, DoublePredicate[] predicates, int i) {
        if (i >= predicates.length) {
            return acc;
        }
        return buildConjunction(acc.and(predicates[i]), predicates, i + 1);
    }

    /**
     * This predicate requires the double to be:
     * d >= -20 * Math.PI && d <= 10 * Math.E OR Math.sin(d) > Math.cos(d) OR d < Math.pow(Math.log(d), 3)
     * AND
     * not too far from n + 0.5 for some n, that is a natural number in [0, 999]
     *
     * @return the predicate
     */
    public DoublePredicate getDefaultComplexPredicate() {
        DoublePredicate[][] predicates = new DoublePredicate[3][];
        final var largeArraySize = 1000;
        predicates[0] = new EpsilonEnvironmentPred[largeArraySize];
        predicates[1] = new DoublePredicate[largeArraySize];
        for (int i = 0; i < largeArraySize; i++) {
            predicates[0][i] = new EpsilonEnvironmentPred(i + 0.5, i / 50000.0);
            int backwardsI = i + 1;
            predicates[1][largeArraySize - backwardsI] = d -> Math.abs(1000 - backwardsI + 0.5 - d) < backwardsI / 50000.0;
        }
        predicates[2] = new DoublePredicate[]{
            d -> d >= -20 * Math.PI && d <= 10 * Math.E,
            d -> Math.sin(d) > Math.cos(d),
            d -> d < Math.pow(Math.log(d), 3),
        };
        return buildComplexPredicate(predicates);
    }

    /**
     * Returns a predicate configured by decimalPlaces and divisor, so that any value satisfies the predicate, iff
     * the sum of its first "decimalPlaces" digits of its decimalPlaces in decimal notation is divisible by divisor.
     *
     * @param decimalPlaces the number of decimal places to consider (e.g. decimalPlaces = 3 and input 1.23456 leads to 234
     * @param divisor       the divisor to divide the sum of decimalPlaces by
     * @return the predicate described above
     */
    public DoublePredicate getChecksumPredicate(int decimalPlaces, int divisor) {
        return (double value) -> {
            var asString = "" + value;
            var checksum = 0;
            var chars = asString.split("\\.")[1].toCharArray();
            for (var i = 0; i < decimalPlaces && i < chars.length; i++) {
                checksum += chars[i] - 48;
            }
            return checksum % divisor == 0;
        };
    }
}
