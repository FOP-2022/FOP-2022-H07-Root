package H07.person;

@FunctionalInterface
public interface PersonFilter {
    boolean test(Person t);
}
