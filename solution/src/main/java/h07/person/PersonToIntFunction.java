package h07.person;

/**
 * A function that maps a {@link Person} to an {@code int}.
 */
@FunctionalInterface
public interface PersonToIntFunction {
    /**
     * Maps p to an int.
     *
     * @param p a {@link Person}
     * @return the int
     */
    int apply(Person p);
}
