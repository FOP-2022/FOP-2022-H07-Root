package h07.person;

import java.util.function.IntBinaryOperator;

public class Traits {
    private final IntBinaryOperator op;
    private final int init;
    private final PersonToIntFunction fct;
    private final PersonFilter pred;
    private final IntBinaryOperator combine;

    /**
     * Constructs the object. See {@link FunctionWithFilterMapAndFold} for usage
     *
     * @param op      the foldl operator
     * @param init    the initial value for foldl
     * @param fct     the mapping function
     * @param pred    the predicate used for filtering
     * @param combine the combination function for two ints
     */
    public Traits(IntBinaryOperator op, int init, PersonToIntFunction fct, PersonFilter pred, IntBinaryOperator combine) {
        this.op = op;
        this.init = init;
        this.fct = fct;
        this.pred = pred;
        this.combine = combine;
    }

    /**
     * the foldl operator
     *
     * @return op
     */
    public IntBinaryOperator getOp() {
        return op;
    }

    /**
     * the initial value for foldl
     *
     * @return init
     */
    public int getInit() {
        return init;
    }

    /**
     * the mapping function
     *
     * @return fct
     */
    public PersonToIntFunction getFct() {
        return fct;
    }

    /**
     * the predicate used for filtering
     *
     * @return pred
     */
    public PersonFilter getPred() {
        return pred;
    }

    /**
     * the combination function for two ints
     *
     * @return combine
     */
    public IntBinaryOperator getCombine() {
        return combine;
    }
}
