package H07;

import H07.person.MyFunction;
import H07.person.Person;
import H07.person.Traits;

public class Main {

  public static void main(String[] args) {
    new MyFunction(new Traits((a, b) -> a + b + 1, 357, p -> p.postalCode, p -> p.lastName.equals("cake"), null)).apply(new Person[]{new Person("hi", "", "", 12, 21)});
  }
}
