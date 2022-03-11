package h07.person;

/**
 * H2.1
 */
public class Person {
    private String lastName;
    private String firstName;
    private String street;
    private int houseNumber;
    private int postalCode;

    /**
     * Constructs a {@code Person} Object.
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
     * The lastname of the person.
     *
     * @return The last name of the person
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName of the person.
     *
     * @param lastName The lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * The firstname of the person.
     *
     * @return The first name of the person
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstName of the person.
     *
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * The street of the person.
     *
     * @return The street of the person
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street of the person.
     *
     * @param street The street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * The houseNumber of the person.
     *
     * @return The house number of the person
     */
    public int getHouseNumber() {
        return houseNumber;
    }

    /**
     * Sets the house number of the person.
     *
     * @param houseNumber The house number to set
     */
    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * The postal code of the person.
     *
     * @return The postal code of the person
     */
    public int getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the person.
     *
     * @param postalCode The postalCode to set
     */
    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
