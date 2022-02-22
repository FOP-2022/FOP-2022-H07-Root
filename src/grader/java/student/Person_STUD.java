package student;

import reflection.*;
import tutor.Mocked;

import java.lang.reflect.Modifier;
import java.util.List;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public class Person_STUD implements Mocked {

    private static ClassTester<?> cPerson;
    private static AttributeTester aLastName;
    private static AttributeTester aFirstName;
    private static AttributeTester aStreet;
    private static AttributeTester aHouseNumber;
    private static AttributeTester aPostalCode;
    private static MethodTester mConstructor;
    private static MethodTester mSetLastName;
    private static MethodTester mGetLastName;
    private static MethodTester mSetFirstName;
    private static MethodTester mGetFirstName;
    private static MethodTester mSetStreet;
    private static MethodTester mGetStreet;

    private static MethodTester mSetHouseNumber;
    private static MethodTester mGetHouseNumber;
    private static MethodTester mSetPostalCode;

    private static MethodTester mGetPostalCode;
    private final Object object;

    public Person_STUD(Object object) {
        this.object = requireNonNull(object);
    }

    public Person_STUD() {
        object = cPerson().instantiate();
    }

    public Person_STUD(String lastName, String firstName, String street, int houseNumber, int postalCode) {
        if (mConstructor().resolved())
            object = mConstructor().invokeStatic(lastName, firstName, street, houseNumber, postalCode);
        else {
            object = cPerson().instantiate();
            setLastName(lastName);
            setFirstName(firstName);
            setStreet(street);
            setHouseNumber(houseNumber);
            setPostalCode(postalCode);
        }
    }

    public static ClassTester<?> cPerson() {
        return (cPerson = requireNonNullElseGet(cPerson, () -> new ClassTester<>(
            "h07.person",
            "Person",
            SIMILARITY,
            PUBLIC
        ))).assureResolved();
    }

    public static AttributeTester aLastName() {
        return aLastName = requireNonNullElseGet(aLastName, () ->
            new AttributeTester()
                .setClassTester(cPerson())
                .setMatcher(
                    new AttributeMatcher(
                        "lastName",
                        SIMILARITY,
                        Modifier.PRIVATE,
                        String.class)).assureResolved());
    }

    public static AttributeTester aFirstName() {
        return aFirstName = requireNonNullElseGet(aFirstName, () ->
            new AttributeTester()
                .setClassTester(cPerson())
                .setMatcher(
                    new AttributeMatcher(
                        "firstName",
                        SIMILARITY,
                        Modifier.PRIVATE,
                        String.class)).assureResolved());
    }

    public static AttributeTester aStreet() {
        return aStreet = requireNonNullElseGet(aStreet, () ->
            new AttributeTester()
                .setClassTester(cPerson())
                .setMatcher(
                    new AttributeMatcher(
                        "street",
                        SIMILARITY,
                        Modifier.PRIVATE,
                        String.class)).assureResolved());
    }

    public static AttributeTester aHouseNumber() {
        return aHouseNumber = requireNonNullElseGet(aHouseNumber, () ->
            new AttributeTester()
                .setClassTester(cPerson())
                .setMatcher(
                    new AttributeMatcher(
                        "houseNumber",
                        SIMILARITY,
                        Modifier.PRIVATE,
                        int.class)).assureResolved());
    }

    public static AttributeTester aPostalCode() {
        return aPostalCode = requireNonNullElseGet(aPostalCode, () ->
            new AttributeTester()
                .setClassTester(cPerson())
                .setMatcher(
                    new AttributeMatcher(
                        "postalCode",
                        SIMILARITY,
                        Modifier.PRIVATE,
                        int.class)).assureResolved());
    }

    public static MethodTester mConstructor() {
        return mConstructor = requireNonNullElseGet(mConstructor, () -> new MethodTester(
            cPerson(),
            "h07.person.Person",
            100,
            PUBLIC | STATIC,
            cPerson().getActualClass(),
            List.of(
                new ParameterMatcher(String.class),
                new ParameterMatcher(String.class),
                new ParameterMatcher(String.class),
                new ParameterMatcher(int.class),
                new ParameterMatcher(int.class)
            )
        ).assureResolved());
    }

    public static MethodTester mSetLastName() {
        return mSetLastName = requireNonNullElseGet(mSetLastName, () -> new MethodTester(
            cPerson(),
            "setLastName",
            SIMILARITY,
            PUBLIC,
            void.class,
            List.of(new ParameterMatcher(String.class))
        ).assureResolved());
    }

    public static MethodTester mGetLastName() {
        return mGetLastName = requireNonNullElseGet(mGetLastName, () -> new MethodTester(
            cPerson(),
            "getLastName",
            SIMILARITY,
            PUBLIC,
            String.class,
            List.of()
        ).assureResolved());
    }

    public static MethodTester mSetFirstName() {
        return mSetFirstName = requireNonNullElseGet(mSetFirstName, () -> new MethodTester(
            cPerson(),
            "setFirstName",
            SIMILARITY,
            PUBLIC,
            void.class,
            List.of(new ParameterMatcher(String.class))
        ).assureResolved());
    }

    public static MethodTester mGetFirstName() {
        return mGetFirstName = requireNonNullElseGet(mGetFirstName, () -> new MethodTester(
            cPerson(),
            "getFirstName",
            SIMILARITY,
            PUBLIC,
            String.class,
            List.of()
        ).assureResolved());
    }

    public static MethodTester mSetStreet() {
        return mSetStreet = requireNonNullElseGet(mSetStreet, () -> new MethodTester(
            cPerson(),
            "setStreet",
            SIMILARITY,
            PUBLIC,
            void.class,
            List.of(new ParameterMatcher(String.class))
        ).assureResolved());
    }

    public static MethodTester mGetStreet() {
        return mGetStreet = requireNonNullElseGet(mGetStreet, () -> new MethodTester(
            cPerson(),
            "getStreet",
            SIMILARITY,
            PUBLIC,
            String.class,
            List.of()
        ).assureResolved());
    }

    public static MethodTester mSetHouseNumber() {
        return mSetHouseNumber = requireNonNullElseGet(mSetHouseNumber, () -> new MethodTester(
            cPerson(),
            "setHouseNumber",
            SIMILARITY,
            PUBLIC,
            void.class,
            List.of(new ParameterMatcher(int.class))
        ).assureResolved());
    }

    public static MethodTester mGetHouseNumber() {
        return mGetHouseNumber = requireNonNullElseGet(mGetHouseNumber, () -> new MethodTester(
            cPerson(),
            "getHouseNumber",
            SIMILARITY,
            PUBLIC,
            int.class,
            List.of()
        ).assureResolved());
    }


    public static MethodTester mSetPostalCode() {
        return mSetPostalCode = requireNonNullElseGet(mSetPostalCode, () -> new MethodTester(
            cPerson(),
            "setPostalCode",
            SIMILARITY,
            PUBLIC,
            void.class,
            List.of(new ParameterMatcher(int.class))
        ).assureResolved());
    }

    public static MethodTester mGetPostalCode() {
        return mGetPostalCode = requireNonNullElseGet(mGetPostalCode, () -> new MethodTester(
            cPerson(),
            "getPostalCode",
            SIMILARITY,
            PUBLIC,
            int.class,
            List.of()
        ).assureResolved());
    }

    public String getLastName() {
        return mGetLastName().invoke(object);
    }

    public void setLastName(String lastName) {
        mSetLastName().invoke(object, lastName);
    }

    public String getFirstName() {
        return mGetFirstName().invoke(object);
    }

    public void setFirstName(String firstName) {
        mSetFirstName().invoke(object, firstName);
    }

    public String getStreet() {
        return mGetStreet().invoke(object);
    }

    public void setStreet(String street) {
        mSetStreet().invoke(object, street);
    }

    public int getHouseNumber() {
        return mGetHouseNumber().invoke(object);
    }

    public void setHouseNumber(int houseNumber) {
        mSetHouseNumber().invoke(object, houseNumber);
    }

    public int getPostalCode() {
        return mGetPostalCode().invoke(object);
    }

    public void setPostalCode(int postalCode) {
        mSetPostalCode().invoke(object, postalCode);
    }

    @Override
    public Object getActualObject() {
        return object;
    }
}
