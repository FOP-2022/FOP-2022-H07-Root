package H07.person;

@FunctionalInterface
public interface PersonToIntFunction {
  /**
   * Maps p to an int
   *
   * @param p a {@link Person}
   * @return the int
   */
  int apply(Person p);
}
