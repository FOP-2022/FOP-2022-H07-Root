package h07.person;

/**
 * A utility class with static methods to create a {@link FunctionWithFilterMapAndFold}.
 */
public class PersonFunctionFactory {
    private static boolean firstImplementationActive;

    /**
     * Whether the first implementation is active.
     *
     * @return The value of {@code firstImplementationActive}
     */
    public boolean isFirstImplementationActive() {
        return firstImplementationActive;
    }

    /**
     * Set firstImplementationActive.
     *
     * @param firstImplementationActive The firstImplementationActive to set
     */
    public void setFirstImplementationActive(boolean firstImplementationActive) {
        PersonFunctionFactory.firstImplementationActive = firstImplementationActive;
    }

    /**
     * Constructs either {@link MyFunctionWithFilterMapAndFold1} or
     * {@link MyFunctionWithFilterMapAndFold2} according to {@link PersonFunctionFactory#isFirstImplementationActive()}.
     *
     * @param traits the traits to configure the {@link FunctionWithFilterMapAndFold} with
     * @return the function
     */
    public FunctionWithFilterMapAndFold createFunctionWithFilterMapAndFold(Traits traits) {
        return firstImplementationActive
            ? new MyFunctionWithFilterMapAndFold1(traits)
            : new MyFunctionWithFilterMapAndFold2(traits);
    }

    /**
     * Constructs the strange function as defined in the exercise.
     *
     * @param name the name to compare to
     * @return the function
     */
    public FunctionWithFilterMapAndFold createStrangeFunction(String name) {
        Traits traits = new Traits(
            (a, b) -> a + b + 1,
            357,
            Person::getPostalCode,
            p -> p.getLastName().equals(name),
            null
        );
        return createFunctionWithFilterMapAndFold(traits);
    }

    /**
     * Constructs the total distance computation function as defined in the exercise.
     *
     * @return the function as a {@link MyFunctionWithAdjacent}
     */
    public FunctionWithFilterMapAndFold distance() {
        Traits traits = new Traits(
            Integer::sum,
            0,
            Person::getPostalCode,
            p -> p.getPostalCode() != 64289,
            (a, b) -> Math.abs(a - b)
        );
        return new MyFunctionWithAdjacent(traits);
    }
}
