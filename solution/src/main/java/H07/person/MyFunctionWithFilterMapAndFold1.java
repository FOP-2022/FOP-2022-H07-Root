package H07.person;

import java.util.function.IntBinaryOperator;

public class MyFunctionWithFilterMapAndFold1 extends FunctionWithFilterMapAndFold {
  public MyFunctionWithFilterMapAndFold1(Traits traits) {
    super(traits);
  }

  public static Person[] filter(Person[] people, PersonFilter filter) {
    Person[] filtered = new Person[people.length];
    int n = 0;
    for (Person p : people) {
      if (filter.test(p)) {
        filtered[n++] = p;
      }
    }
    Person[] result = new Person[n];
    if (n >= 0) System.arraycopy(filtered, 0, result, 0, n);
    return result;
  }

  public static int[] map(Person[] people, PersonToIntFunction map) {
    int[] toInts = new int[people.length];
    for (int i = 0; i < people.length; i++) {
      toInts[i] = map.apply(people[i]);
    }
    return toInts;
  }

  public static int foldl(int[] ints, int init, IntBinaryOperator operator) {
    int acc = init;
    for (var val : ints) {
      acc = operator.applyAsInt(acc, val);
    }
    return acc;
  }

  @Override
  public int apply(Person[] people) {
    var filtered = filter(people, traits.getPred());
    var mapped = map(filtered, traits.getFct());
    return foldl(mapped, traits.getInit(), traits.getOp());
  }
}
