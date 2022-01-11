package h07.person;

/**
 * H2.3
 */
public abstract class FunctionWithFilterMapAndFold {
    protected final Traits traits;

    /**
     * Defines the required argument of {@link Traits} for all subclasses. To be used with super.
     *
     * @param traits {@link Traits} to configure the functions effect
     */
    public FunctionWithFilterMapAndFold(Traits traits) {
        this.traits = traits;
    }

    /**
     * Apply filter, map and fold to the input array, according to {@link Traits}.
     *
     * @param people an array of {@link Person} that should be processed
     * @return the filtered, mapped and folded result
     */
    public abstract int apply(Person[] people);
}
