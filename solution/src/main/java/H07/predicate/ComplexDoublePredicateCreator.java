package H07.predicate;

import java.util.function.DoublePredicate;

public class ComplexDoublePredicateCreator {

    public static DoublePredicate makeComplexPredicate(DoublePredicate[][] predicates) {
        return makeComplexPredicate(makeDisjunction(predicates[0], true), predicates, 1);
    }

    public static DoublePredicate makeDisjunction(DoublePredicate[] predicate, boolean forward) {
        DoublePredicate result;
        if (forward) {
            result = predicate[0];
            for (int i = 1; i < predicate.length; i++) {
                result = result.or(predicate[i]);
            }
        } else {
            result = predicate[predicate.length - 1];
            for (int i = predicate.length - 2; i >= 0; i--) {
                result = result.or(predicate[i]);
            }
        }
        return result;
    }

    public static DoublePredicate makeComplexPredicate(DoublePredicate acc, DoublePredicate[][] predicates, int i) {
        if (i >= predicates.length) {
            return acc;
        }
        return makeComplexPredicate(acc.and(makeDisjunction(predicates[i], i % 2 == 0)), predicates, i + 1);
    }

    public static DoublePredicate getDefaultComplexPredicate() {
        DoublePredicate[][] predicates = new DoublePredicate[3][];
        final var LARGE_ARRAY_SIZE = 1000;
        predicates[0] = new DoublePredicate[LARGE_ARRAY_SIZE];
        predicates[1] = new DoublePredicate[LARGE_ARRAY_SIZE];
        for (int i = 0; i < LARGE_ARRAY_SIZE; i++) {
            predicates[0][i] = new EpsilonEnvironmentPred(i + 0.5, i / 50000.0);
            int finalI = i;
            predicates[1][LARGE_ARRAY_SIZE - i - 1] = d -> Math.abs(finalI + 0.5 - d) < (1000 - finalI) / 50000.0;
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
        return makeComplexPredicate(predicates);
    }
}
