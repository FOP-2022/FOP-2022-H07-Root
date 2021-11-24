package H07.person;

public class PersonFunctionCreator {
  private static boolean firstImplementationActive;

  /**
   * is firstImplementationActive true
   *
   * @return firstImplementationActive
   */
  public static boolean isFirstImplementationActive() {
    return firstImplementationActive;
  }

  /**
   * Set firstImplementationActive
   *
   * @param firstImplementationActive the firstImplementationActive to set
   */
  public static void setFirstImplementationActive(boolean firstImplementationActive) {
    PersonFunctionCreator.firstImplementationActive = firstImplementationActive;
  }

  /**
   * Constructs either {@link MyFunctionWithFilterMapAndFold1} or {@link MyFunctionWithFilterMapAndFold2} according to {{@link PersonFunctionCreator#isFirstImplementationActive()}}
   *
   * @param traits the traits to configure the {@link FunctionWithFilterMapAndFold} with
   * @return the function
   */
  public static FunctionWithFilterMapAndFold createFunctionWithFilterMapAndFold(Traits traits) {
    return firstImplementationActive ? new MyFunctionWithFilterMapAndFold1(traits) : new MyFunctionWithFilterMapAndFold2(traits);
  }

  /**
   * Constructs the particular function as defined in the exercise
   *
   * @param name the name to compare to
   * @return the function
   */
  public static FunctionWithFilterMapAndFold combinedFct(String name) {
    Traits traits = new Traits((a, b) -> a + b + 1, 357, p -> p.postalCode, p -> p.lastName.equals(name), null);
    return PersonFunctionCreator.createFunctionWithFilterMapAndFold(traits);
  }

  /**
   * Constructs the total distance computation function as defined in the exercise
   *
   * @return the function as a {@link MyFunctionWithAdjacent}
   */
  public static FunctionWithFilterMapAndFold distance() {
    Traits traits = new Traits(Integer::sum, 0, p -> p.postalCode, p -> p.postalCode != 64289, (a, b) -> Math.abs(a - b));
    return new MyFunctionWithAdjacent(traits);
  }

  /**
   * Returns the function that can bucket by address
   *
   * @return the function as a {@link MyFunctionWithBucketing}
   */
  public static FunctionWithBucketing bucketingByAddress() {
    return new MyFunctionWithBucketing(((p1, p2) -> p1.postalCode == p2.postalCode && p1.houseNumber == p2.houseNumber && p1.street.equals(p2.street)));
  }
}
