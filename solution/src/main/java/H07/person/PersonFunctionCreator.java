package H07.person;

public class PersonFunctionCreator {
  private static boolean firstImplementationActive;

  public static boolean isFirstImplementationActive() {
    return firstImplementationActive;
  }

  public static void setFirstImplementationActive(boolean firstImplementationActive) {
    PersonFunctionCreator.firstImplementationActive = firstImplementationActive;
  }

  public static FunctionWithFilterMapAndFold createFunctionWithFilterMapAndFold(Traits traits) {
    return firstImplementationActive ? new MyFunctionWithFilterMapAndFold1(traits) : new MyFunctionWithFilterMapAndFold2(traits);
  }

  public static FunctionWithFilterMapAndFold combinedFct(String name) {
    Traits traits = new Traits((a, b) -> a + b + 1, 357, p -> p.postalCode, p -> p.lastName.equals(name), null);
    return PersonFunctionCreator.createFunctionWithFilterMapAndFold(traits);
  }

  public static FunctionWithFilterMapAndFold distance() {
    Traits traits = new Traits((a, b) -> a + b, 0, p -> p.postalCode, p -> p.postalCode != 64289, (a, b) -> Math.abs(a - b));
    return new MyFunctionWithAdjacent(traits);
  }

  public static FunctionWithBucketing bucketingByAddress() {
    return new MyFunctionWithBucketing(((p1, p2) -> p1.postalCode == p2.postalCode && p1.houseNumber == p2.houseNumber && p1.street.equals(p2.street)));
  }
}
