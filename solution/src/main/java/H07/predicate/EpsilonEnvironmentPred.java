package H07.predicate;

import java.util.function.DoublePredicate;

public class EpsilonEnvironmentPred implements DoublePredicate {

    private final double eplison;
    private final double x0;

    public EpsilonEnvironmentPred(double x0, double eplison) {
        this.x0 = x0;
        this.eplison = Math.max(0, eplison);
    }

    @Override
    public boolean test(double value) {
        return Math.abs(value - x0) <= eplison;
    }
}
