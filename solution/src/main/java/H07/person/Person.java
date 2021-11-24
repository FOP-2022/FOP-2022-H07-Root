package H07.person;

public class Person {
  public String lastName;
  public String firstName;
  public String street;
  public int houseNumber;
  public int postalCode;

  /**
   * Constructs a Person Object
   *
   * @param lastName    the lastname
   * @param firstName   the firstname
   * @param street      the street of the persons address
   * @param houseNumber the house number of the persons address
   * @param postalCode  the postal code of the persons address
   */
  public Person(String lastName, String firstName, String street, int houseNumber, int postalCode) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.street = street;
    this.houseNumber = houseNumber;
    this.postalCode = postalCode;
  }
}
