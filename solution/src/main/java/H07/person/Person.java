package H07.person;

public class Person {
  public String lastName;
  public String firstName;
  public String street;
  public int houseNumber;
  public int postalCode;

  public Person(String lastName, String firstName, String street, int houseNumber, int postalCode) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.street = street;
    this.houseNumber = houseNumber;
    this.postalCode = postalCode;
  }

  public int distance(Person other) {
    return Math.abs(other.postalCode - postalCode);
  }

  public Integer getPostalCode() {
    return postalCode;
  }
}
