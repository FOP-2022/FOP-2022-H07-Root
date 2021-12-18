package h07.person;

public class Person {
  private String lastName;
  private String firstName;
  private String street;
  private int houseNumber;
  private int postalCode;

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

  /**
   * the lastname of the person
   *
   * @return lastname
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the lastName
   *
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * the firstname of the person
   *
   * @return firstname
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the firstName
   *
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * the street of the person
   *
   * @return street
   */
  public String getStreet() {
    return street;
  }

  /**
   * Sets the street
   *
   * @param street the street to set
   */
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * the houseNumber of the person
   *
   * @return houseNumber
   */
  public int getHouseNumber() {
    return houseNumber;
  }

  /**
   * Sets the houseNumber
   *
   * @param houseNumber the houseNumber to set
   */
  public void setHouseNumber(int houseNumber) {
    this.houseNumber = houseNumber;
  }

  /**
   * the postalCode of the person
   *
   * @return postalCode
   */
  public int getPostalCode() {
    return postalCode;
  }

  /**
   * Sets the postalCode
   *
   * @param postalCode the postalCode to set
   */
  public void setPostalCode(int postalCode) {
    this.postalCode = postalCode;
  }
}
