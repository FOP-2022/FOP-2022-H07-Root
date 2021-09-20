package H07.person;

@FunctionalInterface
public interface PersonEquivalency {
  boolean test(Person p1, Person p2);
}
