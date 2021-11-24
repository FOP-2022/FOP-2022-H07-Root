package H07.person;

public class MyFunctionWithBucketing extends FunctionWithBucketing {

  public MyFunctionWithBucketing(PersonEquivalency eq) {
    super(eq);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Person[][] apply(Person[] people) {
    var collect = new Person[people.length][];
    var leftPeople = people;
    var collectIdx = 0;
    while (leftPeople.length > 0) {
      Person[] finalLeftPeople = leftPeople;
      var matched = MyFunctionWithFilterMapAndFold1.filter(leftPeople, p -> eq.test(finalLeftPeople[0], p));
      leftPeople = MyFunctionWithFilterMapAndFold1.filter(leftPeople, p -> !eq.test(finalLeftPeople[0], p));
      collect[collectIdx++] = matched;
    }
    var result = new Person[collectIdx][];
    System.arraycopy(collect, 0, result, 0, collectIdx);
    return result;
  }
}
