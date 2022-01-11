package h07.person;

/**
 * A predicate that tests a {@link Person}.
 */
@FunctionalInterface
public interface PersonFilter {
    /**
     * Test if p satisfies the condition.
     *
     * @param p The {@link Person} to test
     * @return Whether p satisfies the condition
     */
    boolean test(Person p);
}
