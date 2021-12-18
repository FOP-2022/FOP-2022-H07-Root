package h07.person;

@FunctionalInterface
public interface PersonFilter {
  /**
   * Check if p satisfies a predicate
   *
   * @param p a {@link Person} to test
   * @return true if p satisfies the predicate
   */
  boolean test(Person p);
}
