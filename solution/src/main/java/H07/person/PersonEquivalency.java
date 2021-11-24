package H07.person;

@FunctionalInterface
public interface PersonEquivalency {

  /**
   * Checks if two {@link Person} are equivalent to some relation.
   * Implementation should follow the definition of an equivalency relation.
   *
   * @param p1 {@link Person}1
   * @param p2 {@link Person}2
   * @return true if p1 and p2 are equivalent
   */
  boolean test(Person p1, Person p2);
}
